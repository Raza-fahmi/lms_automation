package ui.base;

import core.config.ConfigReader;
import core.driver.DriverFactory;
import core.driver.DriverManager;
import org.testng.annotations.*;

public class BaseTest {

    @BeforeMethod(alwaysRun = true)
    @Parameters("browser")
    public void setUp(@Optional("chrome") String browser) {
        // create driver dari factory
        DriverManager.setDriver(DriverFactory.createDriver());
        DriverManager.getDriver().manage().window().maximize();

        // Optional: Print info
        System.out.println("Running test on browser: " + browser);
        System.out.println("Base URL: " + getBaseUrl());
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown() {
        DriverManager.quitDriver();
    }

    protected String getBaseUrl() {
        return ConfigReader.getBaseUrl();
    }
}
