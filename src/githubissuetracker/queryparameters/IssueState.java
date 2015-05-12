package githubissuetracker.queryparameters;

/**
 * The IssueState enum represents the "state" query parameter in the GitHub API. It
 * specifies the state of the issues to be retrieved.
 * 
 * @author justinsvegliato
 */
public enum IssueState implements QueryParameter {

  /** This represents "?state=open" */
  OPEN("open"),

  /** This represents "?state=closed" */
  CLOSE("closed"),

  /** This represents "?state=all" */
  ALL("all");

  private static final String PARAMETER_NAME = "state";
  private final String parameterValue;

  private IssueState(String parameterValue) {
    this.parameterValue = parameterValue;
  }

  @Override
  public String getParameterName() {
    return PARAMETER_NAME;
  }

  @Override
  public String getParameterValue() {
    return parameterValue;
  }

}
