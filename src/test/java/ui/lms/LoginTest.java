package ui.lms;

import core.config.ConfigReader;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import ui.base.BaseTest;
import ui.pages.LoginPage;

public class LoginTest extends BaseTest {

    private LoginPage loginPage;

    @BeforeMethod
    public void initPages() {
        loginPage = new LoginPage();
    }

    @Test(description = "Login with valid credentials")
    public void loginSuccess() {
        // Get credentials from ConfigReader
        String email = ConfigReader.getEmail();
        String password = ConfigReader.getPassword();

        // Validate credentials are not null
        Assert.assertNotNull(email, "Email should not be null from config");
        Assert.assertNotNull(password, "Password should not be null from config");

        // Perform login - menggunakan getBaseUrl() dari BaseTest
        loginPage.open(getBaseUrl());
        loginPage.login(email, password);

        // Validate dashboard is displayed
        Assert.assertTrue(loginPage.isDashboardDisplay(),
                "User should be redirected to dashboard page after successful login");
    }
}