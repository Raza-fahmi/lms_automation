package api.services;

import api.client.GraphQLClient;
import api.models.employee.EmployeeInput;
import api.utils.GraphQLQueryLoader;
import io.restassured.response.Response;

import java.util.HashMap;
import java.util.Map;

public class EmployeeService {

    private final GraphQLClient client;

    public EmployeeService() {
        this.client = GraphQLClient.getInstance();
    }

    /**
     * Create new employee
     */
    public Response createEmployee(EmployeeInput input) throws Exception {
        String query = GraphQLQueryLoader.load("mutations/create_employee");

        Map<String, Object> variables = new HashMap<>();
        variables.put("input", input);

        return client.executeWithAuth(query, variables);
    }

    /**
     * Update existing employee
     */
    public Response updateEmployee(String id, EmployeeInput input) throws Exception {
        String query = GraphQLQueryLoader.load("mutations/update_employee");

        Map<String, Object> variables = new HashMap<>();
        variables.put("id", id);
        variables.put("input", input);

        return client.executeWithAuth(query, variables);
    }

    /**
     * Delete employee
     */
    public Response deleteEmployee(String id) throws Exception {
        String query = GraphQLQueryLoader.load("mutations/delete_employee");

        Map<String, Object> variables = new HashMap<>();
        variables.put("id", id);

        return client.executeWithAuth(query, variables);
    }
}