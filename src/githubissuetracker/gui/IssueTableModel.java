package githubissuetracker.gui;

import githubissuetracker.core.GitHubPageNavigator;
import githubissuetracker.models.Issue;
import java.io.IOException;
import java.util.List;
import javax.swing.table.AbstractTableModel;

/**
 * The IssueTableModel class contains all of the issues to be displayed
 * in the GUI.
 * 
 * @author justinsvegliato
 */
public class IssueTableModel extends AbstractTableModel {

  private static final String[] COLUMN_NAMES = {"Title", "Description"};
  private static final Class[] COLUMN_TYPES = {String.class, String.class};
  private static final String UNKNOWN_FIELD_VALUE = "Unknown";

  private final GitHubPageNavigator<Issue> navigator;

  private List<Issue> currentPage = null;

  /**
   * Creates a new IssueTableModel.
   * 
   * @param navigator the source from which the issues will be generated
   */
  public IssueTableModel(GitHubPageNavigator<Issue> navigator) {
    this.navigator = navigator;
  } 

  @Override
  public int getRowCount() {
    return navigator.getPageSize();
  }

  @Override
  public int getColumnCount() {
    return COLUMN_NAMES.length;
  }

  @Override
  public Object getValueAt(int rowIndex, int columnIndex) {
    return getIssueFieldAt(rowIndex, columnIndex);
  }

  @Override
  public Class getColumnClass(int columnIndex) {
    return COLUMN_TYPES[columnIndex];
  }

  @Override
  public String getColumnName(int columnIndex) {
    return COLUMN_NAMES[columnIndex];
  }
  
  /**
   * Populates the table with the initial page of issues.
   * 
   * @return true if there were any entries, otherwise false
   * @throws IOException if the data can't be loaded
   */
  public boolean populate() throws IOException {
    currentPage = navigator.initialize();
    fireTableDataChanged();
    return !currentPage.isEmpty();
  }

  /**
   * Loads the table with the next page of issues.
   * 
   * @return true if there's another page left after the page just loaded
   * @throws IOException if the data can't be loaded
   */
  public boolean pageLeft() throws IOException {
    if (navigator.hasPreviousPage()) {
      currentPage = navigator.getPreviousPage();
      fireTableDataChanged();
    }
    return navigator.hasPreviousPage();
  }

  /**
   * Loads the table with the previous page of issues.
   * 
   * @return true if there's a page before the page just loaded
   * @throws IOException if the data can't be loaded
   */
  public boolean pageRight() throws IOException {
    if (navigator.hasNextPage()) {
      currentPage = navigator.getNextPage();
      fireTableDataChanged();
    }
    return navigator.hasNextPage();
  }

  /**
   * Gets the issue stored at the given row.
   *
   * @param rowIndex the row index of the issue
   * @return the issue at the specified row
   */
  public Issue getIssueAt(int rowIndex) {
    return currentPage.get(rowIndex);
  }

  private Object getIssueFieldAt(int rowIndex, int columnIndex) {
    Issue issue = currentPage.get(rowIndex);
    switch (columnIndex) {
      case 0:
        return issue.getTitle();
      case 1:
        return issue.getBody().isEmpty() ? "No description provided" : issue.getBody();
    }
    return UNKNOWN_FIELD_VALUE;
  }
  
}
