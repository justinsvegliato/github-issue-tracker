package githubissuetracker.queryparameters;

/**
 * The SortCriteria enum represents the "sort" query parameter in the GitHub API. It
 * simply represents how to sort the data.
 * 
 * @author justinsvegliato
 */
public enum SortCriteria implements QueryParameter {

  /** This represents "?sort=created" */
  CREATED("created"),

  /** This represents "?sort=updated" */
  UPDATED("updated"),

  /** This represents "?sort=comments" */
  COMMENTS("comments");

  private static final String PARAMETER_NAME = "sort";
  private final String parameterValue;

  private SortCriteria(String value) {
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
