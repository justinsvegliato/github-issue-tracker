package githubissuetracker.gui;

import githubissuetracker.util.UrlConnectionHandler;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

/**
 * The GitHubIssuerTracker class starts the application.
 * 
 * @author justinsvegliato
 */
public class GitHubIssueTracker {
  
  // As a side note, I originally implemented a threading system that concurrently pulled 
  // every issue from the API at once since it increased the responsiveness of the user
  // interface. Unfortunately, there are two problems with that approach:
  // 
  // (1) Mainly, there's a ton of network overhead.
  //
  // (2) Technically, each page must be pulled sequentially since the link header of the
  // current page contains the link to the next page. To concurrently pull every page at
  // once, we need to assume that the URL of the next page is of a specific pattern (i.e. 
  // "www.example.com?page=2"). This might be a risky assumption since GitHub could represent
  // the next page as "www.example.com?pageId=jhf3hiusc" instead (i.e. they could use a
  // unique ID). As a result, it's best to follow the pointers rather than assume the URL
  // of the next page.
  //
  // (3) We could preload then next page using a background process, but if the user 
  // spam clicks, they'll have to load the page anyway. If anything, (3) is probably 
  // the best approach since users usually spend a fair bit of time on a given page. I'd 
  // probably implement this backgorund process if this were a robust application.
  
  // Some things that bother me and should probably be fixed:
  //
  // (1) The "Loading..." label becomes visible even when accessing a page that has
  // already been cached.
  //
  // (2) I definitely need more error handling. An exception is thrown when (but the 
  // application doesn't crash) when the a host is unreachable.

  private static final Logger logger = Logger.getLogger(GitHubIssueTracker.class.getName());  
  private static final String PROPERTIES_PATH = "githubissuetracker/config/github.properties";
  
  private static String OWNER;
  private static String REPOSITORY;
  private static String AUTHENTICATION_TOKEN;
    
  // Loads the properties file
  static {
    logger.log(Level.CONFIG, "Loading the application properties...");      
    
    InputStream inputStream = UrlConnectionHandler.class.getClassLoader().getResourceAsStream(PROPERTIES_PATH);
    Properties githubProperties = new Properties();
    
    try {            
      githubProperties.load(inputStream);
      OWNER = githubProperties.getProperty("owner");
      REPOSITORY = githubProperties.getProperty("repository");
      AUTHENTICATION_TOKEN = githubProperties.getProperty("authenticationToken");      
    } catch (IOException ex) {
      logger.log(Level.SEVERE, "Failed to load GitHub API properties", ex);
    } finally {
      try {
        inputStream.close();
      } catch (IOException ex) {
        logger.log(Level.WARNING, "Failed to close property file", ex);
      }
    }
  }
  
  /**
   * Starts the application.
   * 
   * @param args 
   */
  public static void main(String[] args) {
    logger.log(Level.INFO, "Starting the application...");
    
    try {
      UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
    } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
      logger.log(Level.WARNING, "Failed to set the look and feel of the user interface", ex);
    }
    
    SwingUtilities.invokeLater(new Runnable() {
      @Override
      public void run() {
        logger.log(Level.INFO, "The application thread has started");        
        new ApplicationFrame(OWNER, REPOSITORY, AUTHENTICATION_TOKEN).setVisible(true);
      }
    });
  }
  
}