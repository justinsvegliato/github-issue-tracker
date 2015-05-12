package githubissuetracker.models;

import java.util.Date;

/**
 * The Comment class stores the information of a comment posted for a 
 * GitHub issue. Jackson translates the JSON received from the API to this object.
 * 
 * @author justinsvegliato
 */
public class Comment {

  private int id;
  private String body;
  private User user;
  private Date createdAt;

  /**
   * Creates a new Comment.
   */
  public Comment() {}

  /**
   * Gets the ID of this comment.
   * 
   * @return the comment id
   */
  public int getId() {
    return id;
  }
  
  /**
   * Gets the comment text that the user has posted.
   * 
   * @return the comment text
   */
  public String getBody() {
    return body;
  }

  /**
   * Gets the user who posted this comment.
   * 
   * @return the user
   */
  public User getUser() {
    return user;
  }
  
  /**
   * Gets the creation date of this comment.
   * 
   * @return the creation date
   */
  public Date getCreatedAt() {
    return createdAt;
  }
  
  @Override
  public String toString() {
    return String.format("{id=[%s], body=[%s]}", id, body);
  }
}
