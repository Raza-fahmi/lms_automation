package api.client;

import core.config.ConfigReader;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import java.util.HashMap;
import java.util.Map;

public class GraphQLClient {

    private static GraphQLClient instance;
    private RequestSpecification requestSpec;
    private String authToken;
    private Map<String, String> cookies;  // Tambahkan untuk menyimpan cookies

    private GraphQLClient() {
        RestAssured.baseURI = ConfigReader.getApiBaseUrl();
        this.cookies = new HashMap<>();
        this.requestSpec = RestAssured.given()
                .contentType("application/json")
                .header("accept", "application/graphql+json, application/json")
                .header("accept-language", "id,en-US;q=0.9,en;q=0.8")
                .header("sec-fetch-dest", "empty")
                .header("sec-fetch-mode", "cors")
                .header("sec-fetch-site", "same-site")
                .log().all();
    }

    public static GraphQLClient getInstance() {
        if (instance == null) {
            instance = new GraphQLClient();
        }
        return instance;
    }

    public void setAuthToken(String token) {
        this.authToken = token;
    }

    // Tambahkan method untuk set cookie
    public void setCookie(String name, String value) {
        this.cookies.put(name, value);
    }

    public void setCookies(Map<String, String> cookies) {
        this.cookies.putAll(cookies);
    }

    public void clearCookies() {
        this.cookies.clear();
    }

    /**
     * Execute GraphQL query tanpa autentikasi
     */
    public Response execute(String query) {
        String body = String.format("{\"query\": \"%s\"}", escapeJson(query));
        return applyCookies(requestSpec).body(body).when().post();
    }

    /**
     * Execute GraphQL query dengan variables
     */
    public Response execute(String query, Map<String, Object> variables) {
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("query", query);
        requestBody.put("variables", variables);

        System.out.println("=== REQUEST ===");
        System.out.println("URL: " + RestAssured.baseURI);
        System.out.println("Body: " + requestBody);
        System.out.println("Cookies: " + cookies);

        Response response = applyCookies(requestSpec).body(requestBody).when().post();

        // Extract cookies dari response dan simpan
        extractAndStoreCookies(response);

        System.out.println("=== RESPONSE ===");
        System.out.println("Status: " + response.statusCode());
        System.out.println("Body: " + response.asString());

        return response;
    }

    /**
     * Execute dengan autentikasi Bearer Token (masih dipertahankan untuk keperluan lain)
     */
    public Response executeWithAuth(String query, Map<String, Object> variables) {
        if (authToken == null && cookies.isEmpty()) {
            throw new IllegalStateException("No authentication. Please login first to get cookie or token.");
        }

        RequestSpecification authSpec = RestAssured.given()
                .contentType("application/json")
                .header("accept", "application/graphql+json, application/json")
                .header("accept-language", "id,en-US;q=0.9,en;q=0.8")
                .log().all();

        // Prioritaskan cookie jika ada
        if (!cookies.isEmpty()) {
            authSpec = applyCookies(authSpec);
        } else if (authToken != null) {
            authSpec.header("Authorization", "Bearer " + authToken);
        }

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("query", query);
        requestBody.put("variables", variables);

        Response response = authSpec.body(requestBody).when().post();
        extractAndStoreCookies(response);

        return response;
    }

    // Helper untuk apply cookies ke request
    private RequestSpecification applyCookies(RequestSpecification spec) {
        if (!cookies.isEmpty()) {
            StringBuilder cookieString = new StringBuilder();
            for (Map.Entry<String, String> cookie : cookies.entrySet()) {
                if (cookieString.length() > 0) {
                    cookieString.append("; ");
                }
                cookieString.append(cookie.getKey()).append("=").append(cookie.getValue());
            }
            spec.header("Cookie", cookieString.toString());
        }
        return spec;
    }

    // Helper untuk extract cookies dari response
    private void extractAndStoreCookies(Response response) {
        Map<String, String> responseCookies = response.getCookies();
        if (responseCookies != null && !responseCookies.isEmpty()) {
            cookies.putAll(responseCookies);
            System.out.println("Cookies extracted from response: " + responseCookies.keySet());
        }
    }

    private String escapeJson(String s) {
        return s.replace("\"", "\\\"").replace("\n", " ");
    }
}