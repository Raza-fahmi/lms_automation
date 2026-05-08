package ui.lms;

import core.config.ConfigReader;
import core.utils.WaitUtils;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import ui.base.BaseTest;
import ui.pages.LoginPage;
import ui.pages.DashboardPage;
import ui.pages.EmployeePage;

public class EmployeeTest extends BaseTest {

    private LoginPage loginPage;
    private DashboardPage dashboardPage;
    private EmployeePage employeePage;

    // Static data dari config (shared across tests)
    private static String employeeName;
    private static String employeeId;
    private static String employeeEmail;
    private static String employeePhone;
    private static String programStudi;
    private static String employeeRole;

    private static String editedName;
    private static String editedEmail;
    private static String editedPhone;
    private static String editedRole;

    private static String timestamp;

    @BeforeClass
    public void loadTestData() {
        // Load data from config file
        timestamp = String.valueOf(System.currentTimeMillis());

        // Use timestamp to make data unique
        employeeName = ConfigReader.getEmployeeName() + " " + timestamp;
        employeeId = ConfigReader.getEmployeeId() + timestamp;
        employeeEmail = timestamp + "." + ConfigReader.getEmployeeEmail();
        employeePhone = ConfigReader.getEmployeePhone();
        programStudi = ConfigReader.getEmployeeProgramStudi();
        employeeRole = ConfigReader.getEmployeeRole();

        editedName = ConfigReader.getEmployeeEditName() + " " + timestamp;
        editedEmail = timestamp + "." + ConfigReader.getEmployeeEditEmail();
        editedPhone = ConfigReader.getEmployeeEditPhone();
        editedRole = ConfigReader.getEmployeeEditRole();

        System.out.println("========== TEST DATA LOADED ==========");
        System.out.println("Employee ID: " + employeeId);
        System.out.println("Employee Name: " + employeeName);
        System.out.println("Employee Email: " + employeeEmail);
        System.out.println("======================================\n");
    }

    @BeforeMethod
    public void setup() {
        // Initialize pages
        loginPage = new LoginPage();
        dashboardPage = new DashboardPage();
        employeePage = new EmployeePage();

        // Login
        String email = ConfigReader.getEmail();
        String password = ConfigReader.getPassword();

        loginPage.open(getBaseUrl());
        loginPage.login(email, password);

        // Verify login success
        Assert.assertTrue(loginPage.isDashboardDisplay(),
                "Should be able to login before employee test");
    }

    @Test(priority = 1, description = "Add new employee")
    public void testAddNewEmployee() {
        System.out.println("========== TEST 1: ADD EMPLOYEE ==========");
        System.out.println("Employee ID: " + employeeId);
        System.out.println("Employee Name: " + employeeName);

        // Open employee page and wait for table to load
        employeePage = dashboardPage.openEmployeePage();
        employeePage.waitForPageLoad();
        employeePage.waitForTableToLoad();

        // Add employee
        employeePage.clickAddEmployee();
        employeePage.fillEmployeeForm(
                employeeName, employeeId, employeeEmail,
                employeePhone, programStudi, employeeRole
        );
        employeePage.submitEmployee();

        // Verify success
        Assert.assertTrue(employeePage.isSuccessToastDisplayed(),
                "Success toast should appear after adding employee");

        System.out.println("✓ Employee added successfully!");
        System.out.println("==========================================\n");
    }

    @Test(priority = 2, description = "Search and Edit employee")
    public void testSearchAndEditEmployee() {
        System.out.println("========== TEST 2: EDIT EMPLOYEE ==========");
        System.out.println("Searching for Employee ID: " + employeeId);

        // Open employee page
        employeePage = dashboardPage.openEmployeePage();
        employeePage.waitForPageLoad();
        waitFor(2000);

        // Debug: Check search field state
        employeePage.debugSearchField();

        // Try search with retry (3 attempts)
        employeePage.searchEmployeeWithRetry(employeeId, 3);
        waitFor(2000);

        // Verify employee exists before edit
        boolean employeeExists = employeePage.isEmployeeExistsById(employeeId);
        System.out.println("Employee found after search: " + employeeExists);

        Assert.assertTrue(employeeExists,
                "Employee with ID " + employeeId + " should be found before edit");

        // Continue with edit...
        employeePage.clickDetailByEmployeeId(employeeId);
        waitFor(1500);

        employeePage.clickEditOnDetailPage();
        waitFor(1000);

        employeePage.updateEmployeeForm(editedName, editedEmail, editedPhone, editedRole);
        employeePage.clickUpdate();

        Assert.assertTrue(employeePage.isSuccessToastDisplayed(),
                "Success toast should appear after updating employee");

        System.out.println("✓ Employee updated successfully!");
        System.out.println("  New Name: " + editedName);
        System.out.println("==========================================\n");
    }

    @Test(priority = 3, description = "Search and Delete employee")
    public void testSearchAndDeleteEmployee() {
        System.out.println("========== TEST 3: DELETE EMPLOYEE ==========");
        System.out.println("Searching for Employee ID: " + employeeId);

        // Open employee page
        employeePage = dashboardPage.openEmployeePage();
        employeePage.waitForPageLoad();
        waitFor(2000);

        // Search with retry
        employeePage.searchEmployeeWithRetry(employeeId, 3);
        waitFor(2000);

        // Verify employee exists
        boolean employeeExists = employeePage.isEmployeeExistsById(employeeId);
        System.out.println("Employee found before delete: " + employeeExists);
        Assert.assertTrue(employeeExists,
                "Employee with ID " + employeeId + " should be found before delete");

        // Delete
        employeePage.clickDetailByEmployeeId(employeeId);
        waitFor(1000);
        employeePage.clickDeleteOnDetailPage();
        waitFor(500);
        employeePage.confirmDelete();

        Assert.assertTrue(employeePage.isSuccessToastDisplayed(),
                "Success toast should appear after deleting employee");

        // Verify deletion
        waitFor(2000);
        employeePage.searchEmployeeWithRetry(employeeId, 1);
        waitFor(2000);

        boolean employeeStillExists = employeePage.isEmployeeExistsById(employeeId);
        System.out.println("Employee found after delete: " + employeeStillExists);
        Assert.assertFalse(employeeStillExists,
                "Employee with ID " + employeeId + " should not be found");

        System.out.println("✓ Employee deleted successfully!");
        System.out.println("==========================================\n");
    }

    private void waitFor(long milliseconds) {
        WaitUtils.waitForMilliseconds(milliseconds);
    }

}