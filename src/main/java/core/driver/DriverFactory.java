package core.driver;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

public class DriverFactory {

    public static WebDriver createDriver(String browser) {

        switch (browser.toLowerCase()) {

            case "chrome":
                WebDriverManager.chromedriver().setup();

                ChromeOptions options = new ChromeOptions();
                options.addArguments("--remote-allow-origins=*");

                return new ChromeDriver(options);

            default:
                throw new RuntimeException("Browser not supported: " + browser);
        }
    }
}