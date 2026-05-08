package ui.pages;

import core.utils.WaitUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import ui.base.BasePage;
import java.time.Duration;

public class DashboardPage extends BasePage {

    private By employeeMenu = By.id("layout-desktop-menu-item-box-employee");

    // Tambahan locator untuk verifikasi dashboard
    private By dashboardTitle = By.xpath("//*[@id=\"__next\"]/div[2]/div[3]/div/div[1]/p");

    /**
     * Membuka halaman Employee dengan wait yang lebih baik
     */
    public EmployeePage openEmployeePage() {
        // Wait for employee menu to be clickable
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.elementToBeClickable(employeeMenu));

        // Click employee menu
        click(employeeMenu);

        // Wait for page to load - tunggu search field muncul di halaman employee
        wait.until(ExpectedConditions.presenceOfElementLocated(By.id("input-admin-employee-search")));

        // Additional wait for stability
        WaitUtils.waitForMilliseconds(1000);

        return new EmployeePage();
    }

    public boolean isDashboardDisplayed() {
        try {
            return isDisplayed(dashboardTitle);
        } catch (Exception e) {
            return false;
        }
    }
}