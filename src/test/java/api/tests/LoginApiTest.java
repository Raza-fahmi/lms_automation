package api.tests;

import api.client.GraphQLClient;  // Tambahkan import ini
import api.services.AuthService;
import api.utils.GraphQLQueryLoader;
import core.config.ConfigReader;
import io.restassured.response.Response;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class LoginApiTest {

    private GraphQLClient graphQLClient;  // Tambahkan deklarasi ini
    private AuthService authService;
    private String email;
    private String password;
    private String companyId;

    @BeforeClass
    public void setup() {
        // Debug: print semua config values
        System.out.println("=== Reading Config Values ===");

        String apiBaseUrl = ConfigReader.getApiBaseUrl();
        System.out.println("api.base.url: " + apiBaseUrl);

        email = ConfigReader.getTestUserEmail();
        System.out.println("test.user.email: " + email);

        password = ConfigReader.getTestUserPassword();
        System.out.println("test.user.password: " + (password != null ? "********" : "null"));

        companyId = ConfigReader.getTestUserCompanyId();
        System.out.println("test.user.company.id: " + companyId);

        // Validasi: jika ada yang null, throw exception dengan pesan jelas
        if (apiBaseUrl == null || apiBaseUrl.isEmpty()) {
            throw new IllegalArgumentException("api.base.url is not configured in config.properties");
        }
        if (email == null || email.isEmpty()) {
            throw new IllegalArgumentException("test.user.email is not configured in config.properties");
        }
        if (password == null || password.isEmpty()) {
            throw new IllegalArgumentException("test.user.password is not configured in config.properties");
        }
        if (companyId == null || companyId.isEmpty()) {
            throw new IllegalArgumentException("test.user.company.id is not configured in config.properties");
        }

        // Inisialisasi GraphQLClient (tambahkan ini)
        graphQLClient = GraphQLClient.getInstance();
        authService = new AuthService();
        System.out.println("=== Setup completed successfully ===");
    }

    @Test
    public void testLoginSuccess() throws Exception {
        System.out.println("Executing login test...");

        String query = GraphQLQueryLoader.load("mutations/login");

        Map<String, Object> variables = new HashMap<>();
        variables.put("usernameOrEmail", email);
        variables.put("password", password);
        variables.put("companyId", companyId);

        Response response = graphQLClient.execute(query, variables);  // Cookie akan otomatis tersimpan

        assertThat(response.statusCode(), equalTo(200));

        // Parse JSON response
        String username = response.jsonPath().getString("data.login.user.username");
        String userId = response.jsonPath().getString("data.login.user.id");
        String role = response.jsonPath().getString("data.login.user.role");

        System.out.println("Login successful!");
        System.out.println("  Username: " + username);
        System.out.println("  User ID: " + userId);
        System.out.println("  Role: " + role);

        assertThat(username, equalTo("arwendymelyn@dibimbing.id"));
        assertThat(userId, notNullValue());
        assertThat(role, equalTo("admin"));

        // Cookie sudah otomatis tersimpan di graphQLClient, tidak perlu coding tambahan!
    }
}