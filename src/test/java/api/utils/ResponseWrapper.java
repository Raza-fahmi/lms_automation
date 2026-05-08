package api.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.response.Response;

import java.util.List;
import java.util.Map;

public class ResponseWrapper<T> {
    private final Response response;
    private final Class<T> dataClass;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public ResponseWrapper(Response response, Class<T> dataClass) {
        this.response = response;
        this.dataClass = dataClass;
    }

    public int getStatusCode() {
        return response.statusCode();
    }

    public T getData() {
        try {
            // Parse response menjadi JsonNode
            JsonNode rootNode = objectMapper.readTree(response.asString());

            // Ambil field "data" dari response
            JsonNode dataNode = rootNode.get("data");
            if (dataNode == null) {
                return null;
            }

            // Cari field yang berisi data login (misal: "login")
            // Atau langsung convert seluruh dataNode ke T jika sesuai
            // Karena response kita punya structure: data.login.{...}
            JsonNode loginNode = dataNode.get("login");
            if (loginNode != null) {
                // Convert loginNode ke LoginResponse
                return objectMapper.treeToValue(loginNode, dataClass);
            }

            // Fallback: convert entire dataNode
            return objectMapper.treeToValue(dataNode, dataClass);
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse data. Response: " + response.asString(), e);
        }
    }

    public List<Map<String, Object>> getErrors() {
        try {
            JsonNode rootNode = objectMapper.readTree(response.asString());
            JsonNode errorsNode = rootNode.get("errors");
            if (errorsNode != null && errorsNode.isArray()) {
                return objectMapper.convertValue(errorsNode, List.class);
            }
        } catch (Exception e) {
            // No errors or parsing error
        }
        return null;
    }

    public boolean hasErrors() {
        List<Map<String, Object>> errors = getErrors();
        return errors != null && !errors.isEmpty();
    }

    public String getErrorMessage() {
        if (hasErrors()) {
            return getErrors().get(0).get("message").toString();
        }
        return null;
    }
}