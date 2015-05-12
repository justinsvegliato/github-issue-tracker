package githubissuetracker.gui;

import githubissuetracker.core.CommentCache;
import githubissuetracker.core.GitHubApiHandler;
import githubissuetracker.models.Comment;
import githubissuetracker.models.Issue;
import githubissuetracker.queryparameters.SortCriteria;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import javax.swing.table.TableColumn;

/**
 * The ApplicationFrame is the primary frame that contains all other components.
 *
 * @author justinsvegliato
 */
public class ApplicationFrame extends JFrame {

  private static final Logger logger = Logger.getLogger(ApplicationFrame.class.getName());
  private static final String FRAME_TITLE_TEMPLATE = "GitHub Issue Tracker - Repository %s/%s";
  private static final int FRAME_WIDTH = 1000;
  private static final int FRAME_HEIGHT = 580;
  private static final int MARGIN_WIDTH = 10;

  private final GitHubApiHandler apiHandler;
  private final IssueTableModel tableModel;
  private final CommentCache commentCache;

  private final JLabel loadingLabel;
  private final JButton previousButton;
  private final JButton nextButton;

  /**
   * Creates a new ApplicationFrame.
   *
   * @param owner the repository owner
   * @param repository the repository name
   * @param authenticationToken the token used to authenticate GitHub requests
   */
  public ApplicationFrame(String owner, String repository, String authenticationToken) {
    super(String.format(FRAME_TITLE_TEMPLATE, owner, repository));
    setSize(FRAME_WIDTH, FRAME_HEIGHT);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setLocationRelativeTo(null);
    setResizable(false);
    setContentPane(getContentContainer());

    apiHandler = new GitHubApiHandler(authenticationToken);
    tableModel = new IssueTableModel(apiHandler.getIssues(owner, repository, SortCriteria.UPDATED));
    commentCache = new CommentCache(apiHandler);

    loadingLabel = getLoadingLabel();
    previousButton = getPreviousButton();
    nextButton = getNextButton();

    add(getButtonPanel(), BorderLayout.NORTH);
    add(getIssueDisplay(), BorderLayout.CENTER);

    populate();
  }

  // Populates the data in the background to avoid freezing the UI
  private void populate() {
    logger.log(Level.INFO, "Loading the initial page of issues...");
    new SwingWorker<Boolean, Void>() {
      @Override
      protected Boolean doInBackground() throws IOException {
        logger.log(Level.FINE, "Retrieving the initial page of issues in the background...");
        loadingLabel.setVisible(true);        
        return tableModel.populate();
      }

      @Override
      protected void done() {
        logger.log(Level.FINE, "Retrieved the initial page of issues");
        try {
          nextButton.setEnabled(get());
        } catch (InterruptedException | ExecutionException ex) {
          logger.log(Level.SEVERE, "Failed to get the initial issue page", ex);
        }
        loadingLabel.setVisible(false);
      }
    }.execute();
  }

  private JPanel getContentContainer() {
    JPanel contentPanel = new JPanel(new BorderLayout());
    contentPanel.setBorder(BorderFactory.createEmptyBorder(MARGIN_WIDTH, MARGIN_WIDTH, MARGIN_WIDTH, MARGIN_WIDTH));
    return contentPanel;
  }

  private JScrollPane getIssueDisplay() {
    final JTable issueTable = new JTable(tableModel);
    issueTable.getTableHeader().setReorderingAllowed(false);
    issueTable.getTableHeader().setResizingAllowed(false);
    issueTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    issueTable.addMouseListener(new MouseAdapter() {
      @Override
      public void mouseClicked(MouseEvent e) {
        if (SwingUtilities.isLeftMouseButton(e) && e.getClickCount() == 1) {
          Issue issue = tableModel.getIssueAt(issueTable.getSelectedRow());
          try {
            logger.log(Level.INFO, "Retrieving comments for the issue {0}", issue.getNumber());
            List<Comment> comments = commentCache.get(issue);
            new IssueDetailsFrame(issue, comments).setVisible(true);
          } catch (IOException ex) {
            logger.log(Level.SEVERE, "Failed to retrieve comments for the issue " + issue.getNumber(), ex);
          }
        }
      }
    });

    TableColumn descriptionColumn = issueTable.getColumnModel().getColumn(1);
    descriptionColumn.setMaxWidth(400);
    descriptionColumn.setPreferredWidth(400);

    return new JScrollPane(issueTable);
  }

  private JPanel getButtonPanel() {
    JPanel buttonPanel = new JPanel(new BorderLayout());
    buttonPanel.add(previousButton, BorderLayout.LINE_START);
    buttonPanel.add(loadingLabel, BorderLayout.CENTER);
    buttonPanel.add(nextButton, BorderLayout.LINE_END);
    buttonPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 5, 0));
    return buttonPanel;
  }

  private JLabel getLoadingLabel() {
    JLabel label = new JLabel("Loading...");
    label.setFont(new Font("Arial", Font.BOLD, 16));
    label.setHorizontalTextPosition(JLabel.CENTER);
    label.setHorizontalAlignment(JLabel.CENTER);
    label.setVisible(false);
    return label;
  }

  private JButton getPreviousButton() {
    JButton button = getNavigationButton("Previous");
    button.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        logger.log(Level.INFO, "Loading the previous page of issues...");
        try {
          boolean hasPreviousPage = tableModel.pageLeft();
          previousButton.setEnabled(hasPreviousPage);
          nextButton.setEnabled(true);
        } catch (IOException ex) {
          logger.log(Level.SEVERE, "Failed to get previous issue page", ex);
        }
      }
    });
    return button;
  }

  private JButton getNextButton() {
    JButton button = getNavigationButton("Next");
    button.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        // Populates the next page in the background
        logger.log(Level.INFO, "Loading the next page of issues...");
        new SwingWorker<Boolean, Void>() {
          @Override
          protected Boolean doInBackground() throws IOException {
            logger.log(Level.FINE, "Retrieving the next page of issues in the background...");
            nextButton.setEnabled(false);
            previousButton.setEnabled(false);
            loadingLabel.setVisible(true);
            return tableModel.pageRight();
          }

          @Override
          protected void done() {
            logger.log(Level.FINE, "Retrieved the next page of issues");
            try {
              nextButton.setEnabled(get());
              previousButton.setEnabled(true);
              loadingLabel.setVisible(false);
            } catch (InterruptedException | ExecutionException ex) {
              logger.log(Level.SEVERE, "Failed to get next issue page", ex);
            }
          }
        }.execute();
      }
    });
    return button;
  }

  private JButton getNavigationButton(String label) {
    JButton button = new JButton(label);
    button.setPreferredSize(new Dimension(100, 30));    
    button.setEnabled(false);
    return button;
  }

}
