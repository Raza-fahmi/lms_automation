package ui.pages;

import core.driver.DriverManager;
import core.utils.WaitUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.JavascriptExecutor;
import ui.base.BasePage;
import java.time.Duration;
import java.util.List;

public class EmployeePage extends BasePage {

    private WebDriver driver = DriverManager.getDriver();

    // ================= FORM =================
    private By addEmployeeButton = By.id("button-add-employee");
    private By nameField = By.id("name");
    private By employeeIdField = By.id("employeeId");
    private By emailField = By.id("email");
    private By phoneField = By.id("phoneNumber");
    private By programStudiDropdown = By.id("division");
    private By roleField = By.id("employeeRole");
    private By submitButton = By.id("button-add-employee-submit");

    // ================= EDIT FORM =================
    private By editButton = By.xpath("//button[contains(text(),'Edit')]");
    private By updateButton = By.xpath("//button[contains(text(),'Update')]");

    // PERBAIKAN: Gunakan locator edit yang sudah Anda deklarasikan
    private By editNameField = By.id("edit-employee-name-input");
    private By editIdField = By.id("edit-employee-employee-id-input");
    private By editEmailField = By.id("edit-employee-email-input");
    private By editPhoneField = By.id("edit-employee-phone-number-input");
    private By editProgramStudiDropdown = By.id("edit-employee-division-select");
    private By editRoleField = By.id("edit-employee-employee-role-input");
    private By confirmEditButton = By.id("edit-employee-save-changes-button");

    // ================= DELETE CONFIRMATION =================
    private By deleteButton = By.id("delete-employee-button");
    private By confirmDeleteButton = By.id("confirm-delete-button");

    // ================= TOAST =================
    private By successToast = By.cssSelector("#chakra-toast-manager-top div[role='status']");
    private By successToastFallback = By.xpath("//div[@id='chakra-toast-manager-top']//div[contains(@class, 'alert')]");

    // ================= SEARCH =================
    // PERBAIKAN: Locator search yang benar
    private By searchField = By.xpath("//*[@id=\"input-admin-employee-search\"]/input");

    // ================= TABLE =================
    private By employeeTable = By.cssSelector("table, .table, [role='table']");
    private By tableRows = By.cssSelector("tbody tr, .table-row");
    private By loadingIndicator = By.xpath("//*[contains(text(),'Loading') or contains(@class,'loading')]");

    // ================= DYNAMIC LOCATORS =================
    private By detailButtonByEmployeeId(String employeeId) {
        return By.xpath("//td[contains(text(),'" + employeeId + "')]/parent::tr//button[contains(@id, 'button-detail-employee-')]");
    }

    private By employeeRowById(String employeeId) {
        return By.xpath("//td[contains(text(),'" + employeeId + "')]/parent::tr");
    }

    // ================= WAIT METHODS =================

    public EmployeePage waitForPageLoad() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        wait.until(driver -> ((JavascriptExecutor) driver).executeScript("return document.readyState").equals("complete"));
        WaitUtils.waitForMilliseconds(500);
        return this;
    }

    public EmployeePage waitForTableToLoad() {
        System.out.println("Waiting for employee table to load...");
        try {
            WaitUtils.waitForInvisibility(loadingIndicator);
        } catch (Exception e) {
            // No loading indicator
        }

        try {
            WaitUtils.waitForVisibility(employeeTable);
        } catch (Exception e) {
            System.out.println("Table selector not found, continuing...");
        }

        WaitUtils.waitForMilliseconds(1000);
        System.out.println("Table loaded successfully");
        return this;
    }

    public EmployeePage waitForSearchResults() {
        try {
            WaitUtils.waitForInvisibility(loadingIndicator);
        } catch (Exception e) {}
        WaitUtils.waitForMilliseconds(1000);
        return this;
    }

    // ================= SEARCH METHODS =================

    public EmployeePage searchEmployeeSimple(String employeeId) {
        System.out.println("Searching for: " + employeeId);

        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

            // Tunggu search field interactable
            WebElement searchInput = wait.until(ExpectedConditions.elementToBeClickable(searchField));

            // Scroll ke element
            ((JavascriptExecutor) driver).executeScript(
                    "arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});",
                    searchInput
            );
            Thread.sleep(500);

            // Click dengan JS
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", searchInput);
            Thread.sleep(300);

            // Clear
            searchInput.clear();
            ((JavascriptExecutor) driver).executeScript("arguments[0].value = '';", searchInput);
            Thread.sleep(300);

            // Ketik
            searchInput.sendKeys(employeeId);
            Thread.sleep(500);

            // Trigger input event
            ((JavascriptExecutor) driver).executeScript(
                    "arguments[0].dispatchEvent(new Event('input', { bubbles: true }));",
                    searchInput
            );

            // Press Enter
            searchInput.sendKeys(Keys.ENTER);
            Thread.sleep(1500);

            System.out.println("Search completed for: " + employeeId);

        } catch (Exception e) {
            System.out.println("Search failed: " + e.getMessage());
            e.printStackTrace();
        }
        return this;
    }

    public EmployeePage searchEmployeeWithRetry(String employeeId, int maxAttempts) {
        for (int attempt = 1; attempt <= maxAttempts; attempt++) {
            System.out.println("Search attempt " + attempt + " for: " + employeeId);

            try {
                if (attempt > 1) {
                    driver.navigate().refresh();
                    WaitUtils.waitForMilliseconds(2000);
                    waitForTableToLoad();
                }

                searchEmployeeSimple(employeeId);
                WaitUtils.waitForMilliseconds(1500);

                if (isEmployeeExistsById(employeeId)) {
                    System.out.println("Search succeeded on attempt " + attempt);
                    return this;
                }

            } catch (Exception e) {
                System.out.println("Attempt " + attempt + " failed: " + e.getMessage());
            }
        }

        System.out.println("All " + maxAttempts + " search attempts failed for: " + employeeId);
        return this;
    }

    public void debugSearchField() {
        try {
            WebElement searchInput = driver.findElement(searchField);
            System.out.println("=== SEARCH FIELD DEBUG ===");
            System.out.println("Is Displayed: " + searchInput.isDisplayed());
            System.out.println("Is Enabled: " + searchInput.isEnabled());
            System.out.println("Tag Name: " + searchInput.getTagName());
            System.out.println("Current Value: " + searchInput.getAttribute("value"));
        } catch (Exception e) {
            System.out.println("Debug error: " + e.getMessage());
        }
    }

    // ================= ADD EMPLOYEE =================

    public EmployeePage clickAddEmployee() {
        WaitUtils.waitForClickable(addEmployeeButton, 10).click();
        WaitUtils.waitForMilliseconds(1000);
        return this;
    }

    public EmployeePage fillEmployeeForm(
            String name,
            String id,
            String email,
            String phone,
            String programStudiText,
            String role
    ) {
        WaitUtils.waitForVisibility(nameField).sendKeys(name);
        driver.findElement(employeeIdField).sendKeys(id);
        driver.findElement(emailField).sendKeys(email);
        driver.findElement(phoneField).sendKeys(phone);

        if (programStudiText != null && !"Select Program Studi".equals(programStudiText)) {
            selectProgramStudiByText(programStudiText);
        }

        driver.findElement(roleField).sendKeys(role);
        return this;
    }

    public EmployeePage selectProgramStudiByText(String visibleText) {
        if ("Select Program Studi".equals(visibleText)) {
            return this;
        }

        try {
            WebElement dropdown = WaitUtils.waitForVisibility(programStudiDropdown);
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", dropdown);
            WaitUtils.waitForMilliseconds(500);
            Select select = new Select(dropdown);
            select.selectByVisibleText(visibleText);
            WaitUtils.waitForMilliseconds(500);
            System.out.println("Selected program studi: " + visibleText);
        } catch (Exception e) {
            System.out.println("Error selecting program studi: " + e.getMessage());
        }
        return this;
    }

    public EmployeePage submitEmployee() {
        WaitUtils.waitForClickable(submitButton, 10).click();
        WaitUtils.waitForMilliseconds(2000);
        return this;
    }

    // ================= EDIT EMPLOYEE =================

    public EmployeePage clickDetailByEmployeeId(String employeeId) {
        By detailButton = detailButtonByEmployeeId(employeeId);
        WebElement element = WaitUtils.waitForClickable(detailButton, 10);
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
        WaitUtils.waitForMilliseconds(500);
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
        WaitUtils.waitForMilliseconds(1500);
        return this;
    }

    public EmployeePage clickEditOnDetailPage() {
        WebElement editBtn = WaitUtils.waitForClickable(editButton, 10);
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", editBtn);
        WaitUtils.waitForMilliseconds(500);
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", editBtn);
        WaitUtils.waitForMilliseconds(1000);
        return this;
    }

    // PERBAIKAN: Method updateEmployeeForm menggunakan field EDIT
    public EmployeePage updateEmployeeForm(String name, String email, String phone, String role) {
        if (name != null) {
            WebElement nameElement = WaitUtils.waitForVisibility(editNameField);
            nameElement.clear();
            nameElement.sendKeys(name);
            WaitUtils.waitForMilliseconds(300);
        }

        if (email != null) {
            WebElement emailElement = WaitUtils.waitForVisibility(editEmailField);
            emailElement.clear();
            emailElement.sendKeys(email);
            WaitUtils.waitForMilliseconds(300);
        }

        if (phone != null) {
            WebElement phoneElement = WaitUtils.waitForVisibility(editPhoneField);
            phoneElement.clear();
            phoneElement.sendKeys(phone);
            WaitUtils.waitForMilliseconds(300);
        }

        if (role != null) {
            WebElement roleElement = WaitUtils.waitForVisibility(editRoleField);
            roleElement.clear();
            roleElement.sendKeys(role);
            WaitUtils.waitForMilliseconds(300);
        }

        return this;
    }

    public EmployeePage clickUpdate() {
        WaitUtils.waitForClickable(confirmEditButton, 10).click();
        WaitUtils.waitForMilliseconds(2000);
        return this;
    }

    // ================= DELETE EMPLOYEE =================

    public EmployeePage clickDeleteOnDetailPage() {
        WaitUtils.waitForClickable(deleteButton, 10).click();
        WaitUtils.waitForMilliseconds(500);
        return this;
    }

    public EmployeePage confirmDelete() {
        WaitUtils.waitForClickable(confirmDeleteButton, 10).click();
        WaitUtils.waitForMilliseconds(2000);
        return this;
    }

    // ================= VALIDATION =================

    public boolean isEmployeeExistsById(String employeeId) {
        try {
            WaitUtils.waitForVisibility(employeeRowById(employeeId));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isSuccessToastDisplayed() {
        try {
            try {
                return WaitUtils.waitForVisibility(successToast).isDisplayed();
            } catch (Exception e) {
                return WaitUtils.waitForVisibility(successToastFallback).isDisplayed();
            }
        } catch (Exception e) {
            System.out.println("Toast not displayed: " + e.getMessage());
            return false;
        }
    }

    public String getToastMessage() {
        try {
            try {
                return WaitUtils.waitForVisibility(successToast).getText();
            } catch (Exception e) {
                return WaitUtils.waitForVisibility(successToastFallback).getText();
            }
        } catch (Exception e) {
            return "Toast message not found";
        }
    }
}