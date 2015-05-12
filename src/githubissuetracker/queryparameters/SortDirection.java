package githubissuetracker.queryparameters;

/**
 * The SortDirection enum represents the "direction" query parameter in the GitHub API. It
 * chooses the direction in which the data should be sorted.
 * 
 * @author justinsvegliato
 */
public enum SortDirection implements QueryParameter {

  /** This represents "?direction=asc" */
  ASC("asc"),

  /** This represents "?direction=desc" */
  DESC("desc");

  private static final String PARAMETER_NAME = "direction";
  private final String parameterValue;

  private SortDirection(String value) {
    this.parameterValue = value;
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
