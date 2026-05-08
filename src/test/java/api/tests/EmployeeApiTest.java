package api.tests;

import api.client.GraphQLClient;
import api.models.employee.EmployeeInput;
import api.services.EmployeeService;
import api.utils.GraphQLQueryLoader;
import core.config.ConfigReader;
import io.restassured.response.Response;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class EmployeeApiTest {

    private GraphQLClient graphQLClient;
    private EmployeeService employeeService;
    private String createdEmployeeId;
    private String uniqueEmployeeId;
    private String uniqueEmail;

    @BeforeClass
    public void setup() throws Exception {
        System.out.println("=== Setting up Employee API Tests ===");

        // 1. Initialize client and service
        graphQLClient = GraphQLClient.getInstance();
        employeeService = new EmployeeService();

        // 2. Login first to get authentication cookie
        performLogin();

        // 3. Generate unique test data
        uniqueEmployeeId = "AUTO" + System.currentTimeMillis();
        uniqueEmail = "auto.test." + System.currentTimeMillis() + "@example.com";

        System.out.println("=== Setup completed ===");
        System.out.println("Unique Employee ID: " + uniqueEmployeeId);
        System.out.println("Unique Email: " + uniqueEmail);
    }

    private void performLogin() throws Exception {
        String email = ConfigReader.getTestUserEmail();
        String password = ConfigReader.getTestUserPassword();
        String companyId = ConfigReader.getTestUserCompanyId();

        String loginQuery = GraphQLQueryLoader.load("mutations/login");

        Map<String, Object> loginVariables = new HashMap<>();
        loginVariables.put("usernameOrEmail", email);
        loginVariables.put("password", password);
        loginVariables.put("companyId", companyId);

        // Execute login - cookie akan otomatis tersimpan di graphQLClient
        Response loginResponse = graphQLClient.execute(loginQuery, loginVariables);
        assertThat(loginResponse.statusCode(), equalTo(200));

        System.out.println("Login successful, authentication cookie stored");
    }

    @Test(priority = 1)
    public void testCreateEmployee() throws Exception {
        System.out.println("=== Test 1: Create Employee ===");

        EmployeeInput newEmployee = new EmployeeInput.Builder()
                .name("Auto Test Employee")
                .employeeId(uniqueEmployeeId)
                .email(uniqueEmail)
                .phoneNumber("081234567890")
                .divisionId(ConfigReader.getEmployeeDivisionId())
                .employeeRole("Staf")
                .gender("Laki-laki")
                .address("Jl. Test Address No. 123")
                .nik("1234567890123456")
                .npwp("123456789012345")
                .build();

        Response response = employeeService.createEmployee(newEmployee);

        assertThat(response.statusCode(), equalTo(200));

        // Extract created employee ID
        createdEmployeeId = response.jsonPath().getString("data.createEmployee.id");

        System.out.println("Created Employee ID: " + createdEmployeeId);

        assertThat(createdEmployeeId, notNullValue());
        assertThat(createdEmployeeId, not(emptyString()));
    }

    @Test(priority = 2, dependsOnMethods = "testCreateEmployee")
    public void testUpdateEmployee() throws Exception {
        System.out.println("=== Test 2: Update Employee ===");

        EmployeeInput updatedEmployee = new EmployeeInput.Builder()
                .name("Updated Auto Test Employee")
                .employeeId(uniqueEmployeeId)
                .email(uniqueEmail)
                .phoneNumber("089876543210")
                .divisionId(ConfigReader.getEmployeeDivisionId())
                .employeeRole("Senior Staf")
                .gender("Laki-laki")
                .address("Jl. Updated Address No. 456")
                .nik("1234567890123456")
                .npwp("123456789012345")
                .build();

        Response response = employeeService.updateEmployee(createdEmployeeId, updatedEmployee);

        assertThat(response.statusCode(), equalTo(200));

        String updatedId = response.jsonPath().getString("data.updateEmployee.id");

        System.out.println("Updated Employee ID: " + updatedId);

        assertThat(updatedId, equalTo(createdEmployeeId));
    }

    @Test(priority = 3, dependsOnMethods = "testUpdateEmployee")
    public void testDeleteEmployee() throws Exception {
        System.out.println("=== Test 3: Delete Employee ===");

        Response response = employeeService.deleteEmployee(createdEmployeeId);

        assertThat(response.statusCode(), equalTo(200));

        Boolean isDeleted = response.jsonPath().getBoolean("data.deleteEmployee");

        System.out.println("Deleted Employee ID: " + createdEmployeeId);
        System.out.println("Delete result: " + (isDeleted != null && isDeleted ? "SUCCESS" : "FAILED"));

        if (isDeleted != null) {
            assertThat(isDeleted, is(true));
        }
    }
}