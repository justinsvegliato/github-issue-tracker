package githubissuetracker.mappers;

import com.fasterxml.jackson.core.type.TypeReference;
import static githubissuetracker.mappers.GitHubMapper.mapper;
import githubissuetracker.models.Comment;
import java.io.IOException;
import java.util.List;

/**
 * The CommentMapper class converts text to a list of Comment objects.
 * 
 * @author justinsvegliato
 */
public class CommentMapper extends GitHubMapper<Comment> {

  @Override
  public List<Comment> map(String data) throws IOException {
    return mapper.readValue(data, new TypeReference<List<Comment>>() {});
  }

}
