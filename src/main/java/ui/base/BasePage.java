package ui.base;

import core.driver.DriverManager;
import core.listener.TestListener;
import core.utils.WaitUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class BasePage {

    protected WebDriver driver;

    public BasePage() {
        this.driver = DriverManager.getDriver();
    }

    // ================= CLICK =================
    protected void click(By locator) {
        WebElement element = WaitUtils.waitForClickable(locator, 5);

        element.click();

        log("Click on element: " + locator);
    }

    // ================= TYPE =================
    protected void type(By locator, String text) {
        WebElement element = WaitUtils.waitForVisibility(locator);

        element.clear();
        element.sendKeys(text);

        log("Type '" + text + "' into: " + locator);
    }

    // ================= GET TEXT =================
    protected String getText(By locator) {
        WebElement element = WaitUtils.waitForVisibility(locator);

        String text = element.getText();

        log("Get text from " + locator + ": " + text);

        return text;
    }

    // ================= IS DISPLAYED =================
    protected boolean isDisplayed(By locator) {
        try {
            boolean visible = WaitUtils.waitForVisibility(locator).isDisplayed();
            log("Element visible: " + locator);
            return visible;
        } catch (Exception e) {
            log("Element NOT visible: " + locator);
            return false;
        }
    }

    // ================= LOG =================
    protected void log(String message) {
        if (TestListener.getTest() != null) {
            TestListener.getTest().info(message);
        }
    }
}