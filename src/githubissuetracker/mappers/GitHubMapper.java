package githubissuetracker.mappers;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * The GitHubMapper class handles converting data from text to an object. The 
 * subclasses will provide their own map() functions with a concrete class (rather
 * than just a generic type T).
 * 
 * @author justinsvegliato
 * @param <T> the type to convert the text to
 */
public abstract class GitHubMapper<T> {

  /**
   * The mapper to be used by children of GitHubMapper. Theses are settings
   * that should be common to all children given the format of the GitHub API.
   */
  protected static final ObjectMapper mapper = new ObjectMapper() {
    {
      setPropertyNamingStrategy(PropertyNamingStrategy.CAMEL_CASE_TO_LOWER_CASE_WITH_UNDERSCORES);
      setDateFormat(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'"));
      configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }
  };
  
  /**
   * Maps the given data to a list of objects since nobody likes dealing
   * with strings in their code.
   * 
   * @param data the data to be converted to an object
   * @return a list of objects
   * @throws IOException if the data can't be converted to an object
   */
  public abstract List<T> map(String data) throws IOException;

}
