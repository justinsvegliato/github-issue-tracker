package githubissuetracker.mappers;

import com.fasterxml.jackson.core.type.TypeReference;
import githubissuetracker.models.Issue;
import java.io.IOException;
import java.util.List;

/**
 * The IssueMapper class converts text to a list of Issue objects.
 * 
 * @author justinsvegliato
 */
public class IssueMapper extends GitHubMapper<Issue> {

  @Override
  public List<Issue> map(String data) throws IOException {
    return mapper.readValue(data, new TypeReference<List<Issue>>() {});
  }

}
