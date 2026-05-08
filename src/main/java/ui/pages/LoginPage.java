package ui.pages;
import ui.base.BasePage;
import org.openqa.selenium.By;

public class LoginPage extends BasePage {

    // ================= LOCATORS =================
    private By emailField = By.id("input-username-or-email");
    private By passwordField = By.id("input-password");
    private By loginButton = By.id("button-sign-in");
    private By errorMessage = By.xpath("//*[@id=\"__next\"]/div[2]/div[1]/form/div/div[1]/p[2]");
    private By dashboardPage = By.xpath("//*[@id=\"__next\"]/div[2]/div[3]/div/div[1]/p");

    // ================= ACTIONS =================

    public LoginPage open(String url) {
        driver.get(url);
        log("Open URL: " + url);
        return this;
    }

    public LoginPage inputEmail(String email) {
        type(emailField, email);
        return this;
    }

    public LoginPage inputPassword(String password) {
        type(passwordField, password);
        return this;
    }

    public LoginPage clickLogin() {
        click(loginButton);
        return this;
    }

    // ================= BUSINESS FLOW =================

    public void login(String email, String password) {
        inputEmail(email);
        inputPassword(password);
        clickLogin();
    }

    // ================= VALIDATION =================

    public String getErrorMessage() {
        return getText(errorMessage);
    }

    public boolean isErrorDisplayed() {
        return isDisplayed(errorMessage);
    }

    public boolean isDashboardDisplay() {
        try {
            // Coba dengan selector stabil dulu
            return isDisplayed(dashboardPage);
        } catch (Exception e) {
            // Fallback: cari teks dashboard di page source
            return driver.getPageSource().contains("Dashboard") ||
                    driver.findElement(By.tagName("body")).getText().contains("Dashboard");
        }
    }
}