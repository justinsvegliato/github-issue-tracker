package githubissuetracker.gui;

import githubissuetracker.models.Comment;
import githubissuetracker.models.Issue;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;

/**
 * The IssueDetailsFrame displays data associated with a given issue.
 * 
 * @author justinsvegliato
 */
public class IssueDetailsFrame extends JFrame {

  private static final Logger logger = Logger.getLogger(IssueDetailsFrame.class.getName());
  private static final String FRAME_TITLE = "Issue Details";      
  private static final int FRAME_WIDTH = 950;
  private static final int FRAME_HEIGHT = 550;
  private static final int MARGIN_WIDTH = 10;
  
  /**
   * Creates a new IssueDetailsFrame.
   * 
   * @param issue the issue to display data for
   * @param comments the comments to be displayed
   */
  public IssueDetailsFrame(Issue issue, List<Comment> comments) {
    super(FRAME_TITLE);
    
    setSize(FRAME_WIDTH, FRAME_HEIGHT);
    setLocationRelativeTo(null);
    setResizable(false);
    setContentPane(getContentContainer());
    
    add(getHeaderPanel(issue), BorderLayout.PAGE_START);        
    add(getCommentPanel(issue, comments), BorderLayout.CENTER);    
    add(getButtonPanel(), BorderLayout.PAGE_END);
  }
  
  private JPanel getContentContainer() {
    BorderLayout layoutManager = new BorderLayout();
    layoutManager.setVgap(10);
    
    JPanel contentPanel = new JPanel(layoutManager);   
    contentPanel.setBorder(BorderFactory.createEmptyBorder(MARGIN_WIDTH, MARGIN_WIDTH, MARGIN_WIDTH, MARGIN_WIDTH));
    
    return contentPanel;
  }
  
  private JPanel getHeaderPanel(Issue issue) {
    JPanel headerPanel = new JPanel();
    headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.X_AXIS));
        
    JLabel issueNumberLabel = new JLabel(String.format("#%s: ", issue.getNumber()));
    issueNumberLabel.setFont(new Font("Helvetica", Font.BOLD, 24));
    issueNumberLabel.setForeground(Color.GRAY);
    headerPanel.add(issueNumberLabel);
    
    JLabel titleLabel = new JLabel(issue.getTitle());
    titleLabel.setFont(new Font("Helvetica", 0, 24));
    titleLabel.setPreferredSize(new Dimension(titleLabel.getWidth(), titleLabel.getHeight()));
    headerPanel.add(titleLabel);
    
    return headerPanel;
  } 
      
  private JScrollPane getCommentPanel(Issue issue, List<Comment> comments) {
    JPanel commentsPanel = new JPanel();
    commentsPanel.setLayout(new BoxLayout(commentsPanel, BoxLayout.Y_AXIS));
    
    // Creates a panel for the original post (i.e. the description)
    commentsPanel.add(getCommentPanel(issue.getUser().getLogin(), issue.getBody(), issue.getCreatedAt()));
    
    // Creates all of the other comments
    for (Comment comment : comments) {
      commentsPanel.add(getCommentPanel(comment.getUser().getLogin(), comment.getBody(), comment.getCreatedAt()));
    }      
    
    final JScrollPane commentsScroller = new JScrollPane(commentsPanel);
    commentsScroller.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
    
    // Sets the scroll pane to the first comment
    SwingUtilities.invokeLater(new Runnable() {
      @Override
      public void run() { 
        commentsScroller.getVerticalScrollBar().setValue(0);
      }
    });

    return commentsScroller;
  }
  
  private JPanel getCommentPanel(String author, String comment, Date date) {
    JPanel commentPanel = new JPanel();
    commentPanel.setLayout(new BoxLayout(commentPanel, BoxLayout.Y_AXIS));
    
    Border marginBorder = BorderFactory.createEmptyBorder(MARGIN_WIDTH, MARGIN_WIDTH, MARGIN_WIDTH, MARGIN_WIDTH);
    Border lineBorder = BorderFactory.createMatteBorder(1, 0, 1, 0, Color.LIGHT_GRAY);
    Border compoundBorder = BorderFactory.createCompoundBorder(lineBorder, marginBorder);    
 
    // Creates a label that contains the comment's author
    SimpleDateFormat dateFormat = new SimpleDateFormat("MMMMM d, y 'at' h:mm a" );
    String authorLabelText = String.format("%s posted on %s", author, dateFormat.format(date));
    JLabel authorLabel = new JLabel(authorLabelText);
    authorLabel.setFont(new Font("Helvetica", 0, 18));
    authorLabel.setMinimumSize(new Dimension(commentPanel.getWidth(), 0));
    authorLabel.setBorder(marginBorder);
    commentPanel.add(authorLabel);
    
    // Creates a text area that contains the comment
    String text = comment.isEmpty() ? "No description provided" : comment;
    JTextArea commentTextArea = new JTextArea(text);
    commentTextArea.setFont(new Font("Helvetica", 0, 14));
    commentTextArea.setMinimumSize(new Dimension(commentPanel.getWidth(), 0));
    commentTextArea.setLineWrap(true);
    commentTextArea.setWrapStyleWord(true);
    commentTextArea.setEditable(false);    
    commentTextArea.setBorder(compoundBorder);
    commentPanel.add(commentTextArea);
    
    return commentPanel;
  }
  
  private JPanel getButtonPanel() {
    JButton closeButton = new JButton("Close");
    closeButton.setPreferredSize(new Dimension(75, 30));
    closeButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        logger.info("Closing the issue details frame...");
        dispose();
      }
    });
    
    JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
    buttonPanel.add(closeButton);
    
    return buttonPanel;
  }
}