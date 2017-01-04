package rs.acs.uns.sw.e2e.util;

/**
 * Mutual constants for all tests
 */
public interface Constants {

    // Chrome WebDriver constants
    String WEBDRIVER_NAME = "webdriver.chrome.driver";
    Integer WEBDRIVER_TIMEOUT = 20;

    // IMPORTANT: Change this locally to path of your driver
    String WEBDRIVER_PATH = "/Users/dmarjanovic/Downloads/chromedriver";

    String MAXIMIZE_OSX = "--kiosk";
    String MAXIMIZE_WIN = "--start-maximized";
}