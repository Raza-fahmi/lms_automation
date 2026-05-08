package core.utils;

import core.driver.DriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.*;

import java.time.Duration;

public class WaitUtils {

    private static final int TIMEOUT = 10;

    private static WebDriverWait getWait() {
        WebDriver driver = DriverManager.getDriver();
        return new WebDriverWait(driver, Duration.ofSeconds(TIMEOUT));
    }

    public static WebElement waitForVisibility(By locator) {
        return getWait().until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    public static WebElement waitForClickable(By locator, int timeoutInSeconds) {
        WebDriverWait wait = new WebDriverWait(DriverManager.getDriver(), Duration.ofSeconds(timeoutInSeconds));
        return wait.until(ExpectedConditions.elementToBeClickable(locator));
    }

    public static boolean waitForInvisibility(By locator) {
        return getWait().until(ExpectedConditions.invisibilityOfElementLocated(locator));
    }

    public static void waitForText(By locator, String text) {
        getWait().until(ExpectedConditions.textToBePresentInElementLocated(locator, text));
    }

    public static void waitForMilliseconds(long milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Thread was interrupted during sleep", e);
        }
    }
}