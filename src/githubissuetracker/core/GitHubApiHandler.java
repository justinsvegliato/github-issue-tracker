package githubissuetracker.core;

import githubissuetracker.mappers.CommentMapper;
import githubissuetracker.mappers.IssueMapper;
import githubissuetracker.models.Comment;
import githubissuetracker.models.Issue;
import githubissuetracker.queryparameters.QueryParameter;

/**
 * The GitHubApiHandler class provides functions that retrieve data from the GitHub API. Every call
 * to GitHubApiHandler returns a GitHubPageNavigator. This class iterates through the pages returned
 * by a particular query. For instance, if we call getIssues, we receive a GitHubPageNavigator object
 * that traverses through the pages returned from the query.
 *
 * @author justinsvegliato
 */
public class GitHubApiHandler {

  private static final String REPOSITORY_ISSUE_URL_TEMPLATE = "https://api.github.com/repos/%s/%s/issues";
  private static final String QUERY_PARAMETER_TEMPLATE = "%s=%s";
  private static final String QUERY_START_TOKEN = "?";
  private static final String QUERY_CONJUNCTION_TOKEN = "&";

  private final String authenticationToken;

  /**
   * Creates a new GitHubApiHandler.
   * 
   * @param authenticationToken the token used to authenticate GitHub requests
   */
  public GitHubApiHandler(String authenticationToken) {
    this.authenticationToken = authenticationToken;
  }
  
  /**
   * Gets issues from the specified repository.
   *
   * @param owner the repository owner
   * @param repository the repository name
   * @param parameters the query parameters in the URL
   * @return a list of issues
   */
  public GitHubPageNavigator<Issue> getIssues(String owner, String repository, QueryParameter... parameters) {
    String url = String.format(REPOSITORY_ISSUE_URL_TEMPLATE, owner, repository);     
    String modifiedUrl = addQueryParameters(url, parameters);
    return new GitHubPageNavigator<>(modifiedUrl, authenticationToken, new IssueMapper());
  }

  /**
   * Gets the comments for the specified issue.
   *
   * @param issue the issue to retrieve comments for
   * @param parameters the query parameters in the URL
   * @return a list of comments
   */
  public GitHubPageNavigator<Comment> getComments(Issue issue, QueryParameter... parameters) {
    String url = addQueryParameters(issue.getCommentsUrl(), parameters);
    return new GitHubPageNavigator<>(url, authenticationToken, new CommentMapper());
  }
  
  private String addQueryParameters(String url, QueryParameter... parameters) {
    if (parameters.length > 0) {
      url += QUERY_START_TOKEN;
      for (QueryParameter parameter : parameters) {
        String query = String.format(QUERY_PARAMETER_TEMPLATE, parameter.getParameterName(), parameter.getParameterValue());
        url += query + QUERY_CONJUNCTION_TOKEN;
      }
    }
    return url;
  }

}
