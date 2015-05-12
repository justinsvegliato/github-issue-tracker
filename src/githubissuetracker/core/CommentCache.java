package githubissuetracker.core;

import githubissuetracker.models.Comment;
import githubissuetracker.models.Issue;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The CommentStore class retrieves and store comments from GitHub. This primarily reduces 
 * network overhead by minimizing the amount of calls made to the GitHub API.
 *
 * @author justinsvegliato
 */
public class CommentCache {

  private final Map<Integer, List<Comment>> commentsMap = new HashMap<>();

  private final GitHubApiHandler apiHandler;

  /**
   * Creates a new CommentCache. This ensures that we don't grab the comments for a given 
   * page more than once.
   *
   * @param apiHandler the API handler that'll retrieve comments from GitHub
   */
  public CommentCache(GitHubApiHandler apiHandler) {
    this.apiHandler = apiHandler;
  }

  /**
   * Gets the list of comments for a given issue. The comments are stored if they haven't been 
   * retrieved from the GitHub API yet.
   *
   * @param issue the issue to retrieve comments for
   * @return the list of comments for the given issue
   * @throws IOException if the comments can't be retrieved
   */
  public List<Comment> get(Issue issue) throws IOException {
    if (!commentsMap.containsKey(issue.getId())) {
      commentsMap.put(issue.getId(), getComments(issue));
    }
    return commentsMap.get(issue.getId());
  }

  private List<Comment> getComments(Issue issue) throws IOException {
    GitHubPageNavigator<Comment> commentNavigator = apiHandler.getComments(issue);

    // Iterates through every page in the query result
    List<Comment> comments = commentNavigator.initialize();
    while (commentNavigator.hasNextPage()) {
      comments.addAll(commentNavigator.getNextPage());
    }

    return comments;
  }
  
}
