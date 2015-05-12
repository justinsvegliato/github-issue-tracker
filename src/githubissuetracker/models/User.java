package githubissuetracker.models;

/**
 * The User class stores user information of a comment or an issue 
 * retrieved by the GitHub API.
 * 
 * @author justinsvegliato
 */
public class User {
  
  private int id;
  private String login;

  /**
   * Creates a new User.
   */
  public User() {}

  /**
   * Gets the ID of the user.
   * 
   * @return the user id
   */
  public int getId() {
    return id;
  }
  
  /**
   * Gets the name of the user.
   * 
   * @return the username
   */
  public String getLogin() {
    return login;
  }
  
  @Override
  public String toString() {
    return String.format("{id=[%s], login=[%s]}", id, login);
  }
}