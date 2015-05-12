package githubissuetracker.models;

import java.util.Date;

/**
 * The Issue class stores the information of a GitHub issue. Jackson translates 
 * the JSON received from the API to this object.
 *
 * @author justinsvegliato
 */
public class Issue {

  private int id;
  private int number;
  private String title;
  private String body;
  private User user;
  private String commentsUrl;
  private Date createdAt;

  /**
   * Creates a new Issue.
   */
  public Issue() {}

  /**
   * Gets the id of this issue.
   *
   * @return the issue id
   */
  public int getId() {
    return id;
  }

  /**
   * Gets the number of this issue.
   *
   * @return the issue number
   */
  public int getNumber() {
    return number;
  }

  /**
   * Gets the title of this issue.
   *
   * @return the issue title
   */
  public String getTitle() {
    return title;
  }

  /**
   * Gets the description of this issue.
   *
   * @return the issue description
   */
  public String getBody() {
    return body;
  }

  /**
   * Gets the URL of the comments of this issue.
   *
   * @return the comment URL
   */
  public String getCommentsUrl() {
    return commentsUrl;
  }

  /**
   * Gets the creation date of this issue.
   *
   * @return the creation date
   */
  public Date getCreatedAt() {
    return createdAt;
  }

  /**
   * Gets the user who posted this issue.
   *
   * @return the user
   */
  public User getUser() {
    return user;
  }

  @Override
  public String toString() {
    return String.format("{id=[%s], number=[%d], title=[%s], body=[%s]}", id, number, title, body);
  }
}
