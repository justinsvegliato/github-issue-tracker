package githubissuetracker.models;

import java.util.List;

/**
 * The GitHubPage class represents the fields of a page retrieved from the GitHub API. 
 * This includes not only the data from the request body, but the navigation links 
 * retrieved from the request header.
 *
 * @author justinsvegliato
 * @param <T> the type of data in the request body
 */
public class GitHubPage<T> {

  private final int id;
  private final String firstPageUrl;
  private final String previousPageUrl;
  private final String nextPageUrl;
  private final String lastPageUrl;
  private final List<T> entries;

  /**
   * Creates a new GitHubPage class.
   *
   * @param id the id of the page
   * @param firstPageUrl the URL of the first page
   * @param previousPageUrl the URL of the previous page
   * @param nextPageUrl the URL of the next page
   * @param lastPageUrl the URL of the last page
   * @param entries the data retrieved from the page
   */
  public GitHubPage(int id, String firstPageUrl, String previousPageUrl, String nextPageUrl, 
          String lastPageUrl, List<T> entries) {
    this.id = id;
    this.firstPageUrl = firstPageUrl;
    this.previousPageUrl = previousPageUrl;
    this.nextPageUrl = nextPageUrl;
    this.lastPageUrl = lastPageUrl;
    this.entries = entries;
  }

  /**
   * Gets the ID of the page.
   * 
   * @return the id
   */
  public int getId() {
    return id;
  }

  /**
   * Gets the URL of the first page.
   * 
   * @return the URL
   */
  public String getFirstPageUrl() {
    return firstPageUrl;
  }

  /**
   * Gets the URL of the previous page.
   * 
   * @return the URL
   */
  public String getPreviousPageUrl() {
    return previousPageUrl;
  }

  /**
   * Gets the URL of the next page.
   * 
   * @return the URL
   */
  public String getNextPageUrl() {
    return nextPageUrl;
  }

  /**
   * Gets the URL of the last page.
   * 
   * @return the URL
   */
  public String getLastPageUrl() {
    return lastPageUrl;
  }

  /**
   * Gets the entries of the page.
   * 
   * @return the entries
   */
  public List<T> getEntries() {
    return entries;
  }

}
