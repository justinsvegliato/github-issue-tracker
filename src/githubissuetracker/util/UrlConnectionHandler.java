package githubissuetracker.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

/**
 * The UrlHandler class provides functions that deals with connecting to and 
 * receiving data from URLs. While this class currently only has two functions, 
 * it would grow over time as the complexity of the system grows.
 *
 * @author justinsvegliato
 */
public class UrlConnectionHandler {

  // The constructor is private to prevent instantation
  private UrlConnectionHandler() {
    throw new AssertionError();
  }

  /**
   * Gets the data from the specified URL.
   *
   * @param connection the connection to read data from
   * @return the data read from the connection
   * @throws IOException if a connection can't be established
   */
  public static String getUrlText(URLConnection connection) throws IOException {
    BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));

    // We use a builder here since there might be a lot of data. Generically, 
    // builders are must faster than string concatenation. That said, I wonder
    // if the compiler creates a builder automatically.
    StringBuilder builder = new StringBuilder();
    String line;
    while ((line = in.readLine()) != null) {
      builder.append(line);
    }

    return builder.toString();
  }

  /**
   * Gets an authenticated connection (but doesn't pull data) for the specified URL and authentication token.
   *
   * @param rawUrl the URL to pull data from
   * @param authenticationToken the token for Basic Authentication
   * @return the URLConnection object to retrieve data from
   * @throws MalformedURLException if the URL is invalid
   * @throws IOException if the connection cannot be established
   */
  public static URLConnection getAuthenticatedUrlConnection(String rawUrl, String authenticationToken) 
          throws MalformedURLException, IOException {
    URL url = new URL(rawUrl);
    URLConnection connection = url.openConnection();
    connection.setRequestProperty("Authorization", "token " + authenticationToken);
    return connection;
  }
}
