package githubissuetracker.queryparameters;

/**
 * The QueryParameter interface represents the parameters that can embedded into the
 * URL of a request (i.e. "?state=open" or "?direction=asc"). This avoids hard coding
 * these sorts of parameters as strings into the application. 
 * 
 * @author justinsvegliato
 */
public interface QueryParameter {

  /**
   * Gets the name of the query parameter (i.e. "state").
   * 
   * @return the name
   */
  public String getParameterName();

  /**
   * Gets the value of the query parameter (i.e. "open").
   * 
   * @return the value
   */
  public String getParameterValue();

}
