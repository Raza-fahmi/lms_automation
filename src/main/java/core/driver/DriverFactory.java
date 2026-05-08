package core.driver;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

public class DriverFactory {
    
    public static WebDriver createDriver() {
        ChromeOptions options = new ChromeOptions();
        
        // Deteksi apakah running di GitHub Actions
        String ciEnv = System.getenv("CI");
        if (ciEnv != null && ciEnv.equals("true")) {
            // Mode headless untuk CI
            options.addArguments("--headless=new");
            options.addArguments("--no-sandbox");
            options.addArguments("--disable-dev-shm-usage");
            options.addArguments("--disable-gpu");
            options.addArguments("--remote-allow-origins=*");
            options.addArguments("--window-size=1920,1080");
        } else {
            // Mode normal untuk lokal
            options.addArguments("--remote-allow-origins=*");
        }
        
        WebDriverManager.chromedriver().setup();
        return new ChromeDriver(options);
    }
}
