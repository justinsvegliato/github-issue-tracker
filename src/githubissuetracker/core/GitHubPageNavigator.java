package githubissuetracker.core;

import githubissuetracker.mappers.GitHubMapper;
import githubissuetracker.models.GitHubPage;
import githubissuetracker.util.UrlConnectionHandler;
import java.io.IOException;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * The GitHubPageNavigator class handles traversing through page data 
 * returned by a GitHub API query. That is, it allows users to go backward or 
 * forward through the results returned by a query. This class is necessary since 
 * the data returned by GitHub is always paged. Typically, the pages range from 30 
 * to 100 entries; however, since this is just a rough iteration, the page size 
 * can't be changed. 
 * 
 * The pages are also cached and loaded lazily to avoid network overhead and increase
 * the responsiveness of the user interface. 
 * 
 * Note that it's pretty similar to ListIterator (or Iterator) but ListIterator
 * provides too many method (e.g. remove and add) and Iterator only provides next and 
 * hasNext. Unfortunately, while GitHubPageNavigator couldn't implemented Iterator
 * or ListIterator, it seemed somewhat forced.
 * 
 * @author justinsvegliato
 * @param <T> the type of data returned by the URL (i.e. issues or comments)
 */
public class GitHubPageNavigator<T> {

  private static final Pattern PREVIOUS_PAGE_PATTERN = Pattern.compile(".*<(.*)>;\\s+rel=\"prev\"");
  private static final Pattern NEXT_PAGE_PATTERN = Pattern.compile(".*<(.*)>;\\s+rel=\"next\"");
  private static final Pattern FIRST_PAGE_PATTERN = Pattern.compile(".*<(.*)>;\\s+rel=\"first\"");
  private static final Pattern LAST_PAGE_PATTERN = Pattern.compile(".*<(.*)>;\\s+rel=\"last\"");

  private final String queryUrl;
  private final String authenticationToken;
  private final GitHubMapper mapper;
  
  private final Map<Integer, GitHubPage<T>> cache = new HashMap<>();

  private GitHubPage currentPage;

  /**
   * Creates a new GitHubPageNavigator.
   * 
   * @param queryUrl the URL to retrieve paginated data from
   * @param authenticationToken the token needed to authenticate the request
   * @param mapper the mapper that converts the retrieved text to a specific object
   */
  public GitHubPageNavigator(String queryUrl, String authenticationToken, GitHubMapper mapper) {
    this.queryUrl = queryUrl;
    this.authenticationToken = authenticationToken;
    this.mapper = mapper;
  }

  /**
   * Initializes the navigator by loading the first page of the query results. This must 
   * be the first method called. This isn't in the constructor since it returns an 
   * exception and can also be easily called from a separate thread to avoid locking 
   * up the user interface.
   * 
   * @return a list of entries from the first page
   * @throws IOException if the data can't be retrieved from the GitHub API
   */
  public List<T> initialize() throws IOException {
    // Gets the first page of the query URL
    currentPage = getPage(1, queryUrl);
    return currentPage.getEntries();
  }

  /**
   * Retrieves a list of entries from the next page of the query results.
   * 
   * @return a list of entries from the next page
   * @throws IOException if the data can't be retrieved from the GitHub API
   */
  public List<T> getNextPage() throws IOException {
    // Gets the next page of the query URL
    currentPage = getPage(currentPage.getId() + 1, currentPage.getNextPageUrl());
    return currentPage.getEntries();
  }

  /**
   * Retrieves a list of entries from the previous page of the query results.
   * 
   * @return a list of entries from the previous page
   * @throws IOException if the data can't be retrieved from the GitHub API
   */
  public List<T> getPreviousPage() throws IOException {
    currentPage = getPage(currentPage.getId() - 1, currentPage.getPreviousPageUrl());
    return currentPage.getEntries();
  }

  /**
   * Returns true if the query has more next pages.
   * 
   * @return true if there's another page 
   */
  public boolean hasNextPage() {   
    return currentPage.getNextPageUrl() != null;
  }

  /**
   * Returns true if the query has any previous pages.
   * 
   * @return true if there's another page 
   */
  public boolean hasPreviousPage() {
    return currentPage.getPreviousPageUrl() != null;
  }

  /**
   * Gets the size of the current page.
   * 
   * @return the size
   */
  public int getPageSize() {
    return currentPage == null ? 0 : currentPage.getEntries().size();
  }
  
  private GitHubPage getPage(int id, String url) throws IOException {
    if (!cache.containsKey(id)) {
      cache.put(id, loadPageFromApi(id, url));
    }
    return cache.get(id);    
  }

  private GitHubPage<T> loadPageFromApi(int id, String url) throws IOException {
    URLConnection connection = UrlConnectionHandler.getAuthenticatedUrlConnection(url, authenticationToken);
    Map<String, List<String>> headerFields = connection.getHeaderFields();
    
    String firstPageUrl = getPageUrl(FIRST_PAGE_PATTERN, headerFields);
    String previousPageUrl = getPageUrl(PREVIOUS_PAGE_PATTERN, headerFields);
    String nextPageUrl = getPageUrl(NEXT_PAGE_PATTERN, headerFields);
    String lastPageUrl = getPageUrl(LAST_PAGE_PATTERN, headerFields);
    List<T> items = mapper.map(UrlConnectionHandler.getUrlText(connection));

    return new GitHubPage(id, firstPageUrl, previousPageUrl, nextPageUrl, lastPageUrl, items);
  }

  private String getPageUrl(Pattern pattern, Map<String, List<String>> headerFields) {
    // HTTP header must contain the "Link" as an attribute
    if (!headerFields.containsKey("Link")) {
      return null;
    }
    
    // Ensure that there are other navigation pages
    String navigationLink = headerFields.get("Link").get(0);
    if (navigationLink == null) {
      return null;
    }
    
    // Pulls the link from the data
    Matcher matcher = pattern.matcher(navigationLink);
    if (!matcher.find()) {
      return null;
    }
    
    return matcher.group(1);
  }

}