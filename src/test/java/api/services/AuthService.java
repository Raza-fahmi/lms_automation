package api.services;

import api.client.GraphQLClient;
import api.models.login.LoginResponse;
import api.utils.GraphQLQueryLoader;
import api.utils.ResponseWrapper;
import io.restassured.response.Response;

import java.util.HashMap;
import java.util.Map;

public class AuthService {

    private final GraphQLClient client;

    public AuthService() {
        this.client = GraphQLClient.getInstance();
    }

    // Tambahkan method ini untuk mengakses client
    public GraphQLClient getClient() {
        return client;
    }

    public ResponseWrapper<LoginResponse> login(String usernameOrEmail, String password, String companyId) throws Exception {
        String query = GraphQLQueryLoader.load("mutations/login");

        Map<String, Object> variables = new HashMap<>();
        variables.put("usernameOrEmail", usernameOrEmail);
        variables.put("password", password);
        variables.put("companyId", companyId);

        Response response = client.execute(query, variables);
        return new ResponseWrapper<>(response, LoginResponse.class);
    }
}