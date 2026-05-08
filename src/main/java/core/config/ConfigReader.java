package core.config;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ConfigReader {
    private static Properties properties = new Properties();

    static {
        try {
            String env = System.getProperty("env", "staging");
            String configPath = "src/test/resources/config/" + env + ".properties";

            FileInputStream fis = new FileInputStream(configPath);
            properties.load(fis);
            fis.close();

            System.out.println("Config loaded from: " + configPath);
        } catch (IOException e) {
            System.err.println("Config file not found. Using environment variables.");
            // Jika file tidak ada, coba baca dari system properties (GitHub Actions)
            loadFromSystemProperties();
        }
    }

    private static void loadFromSystemProperties() {
        System.out.println("Loading configuration from system properties...");

        // UI Config
        setIfPresent("baseUrl", System.getProperty("baseUrl"));
        setIfPresent("email", System.getProperty("email"));
        setIfPresent("password", System.getProperty("password"));

        // API Config
        setIfPresent("api.base.url", System.getProperty("api.base.url"));
        setIfPresent("test.user.email", System.getProperty("test.user.email"));
        setIfPresent("test.user.password", System.getProperty("test.user.password"));
        setIfPresent("test.user.company.id", System.getProperty("test.user.company.id"));
        setIfPresent("employee.division.id", System.getProperty("employee.division.id"));

        // Employee Data
        setIfPresent("employee.name", System.getProperty("employee.name"));
        setIfPresent("employee.id", System.getProperty("employee.id"));
        setIfPresent("employee.email", System.getProperty("employee.email"));
        setIfPresent("employee.phone", System.getProperty("employee.phone"));
        setIfPresent("employee.program.studi", System.getProperty("employee.program.studi"));
        setIfPresent("employee.role", System.getProperty("employee.role"));

        // Edit Data
        setIfPresent("employee.edit.name", System.getProperty("employee.edit.name"));
        setIfPresent("employee.edit.email", System.getProperty("employee.edit.email"));
        setIfPresent("employee.edit.phone", System.getProperty("employee.edit.phone"));
        setIfPresent("employee.edit.role", System.getProperty("employee.edit.role"));
    }

    private static void setIfPresent(String key, String value) {
        if (value != null && !value.isEmpty()) {
            properties.setProperty(key, value);
            System.out.println("Loaded: " + key);
        }
    }

    // ========== GETTER METHODS (sama seperti sebelumnya) ==========
    public static String getBaseUrl() { return properties.getProperty("baseUrl"); }
    public static String getEmail() { return properties.getProperty("email"); }
    public static String getPassword() { return properties.getProperty("password"); }

    public static String getEmployeeName() { return properties.getProperty("employee.name"); }
    public static String getEmployeeId() { return properties.getProperty("employee.id"); }
    public static String getEmployeeEmail() { return properties.getProperty("employee.email"); }
    public static String getEmployeePhone() { return properties.getProperty("employee.phone"); }
    public static String getEmployeeProgramStudi() { return properties.getProperty("employee.program.studi"); }
    public static String getEmployeeRole() { return properties.getProperty("employee.role"); }

    public static String getEmployeeEditName() { return properties.getProperty("employee.edit.name"); }
    public static String getEmployeeEditEmail() { return properties.getProperty("employee.edit.email"); }
    public static String getEmployeeEditPhone() { return properties.getProperty("employee.edit.phone"); }
    public static String getEmployeeEditRole() { return properties.getProperty("employee.edit.role"); }

    public static String getApiBaseUrl() { return properties.getProperty("api.base.url"); }
    public static int getApiTimeout() {
        String timeout = properties.getProperty("api.timeout", "10000");
        return Integer.parseInt(timeout);
    }
    public static String getApiAuthToken() { return properties.getProperty("api.auth.token"); }
    public static String getTestUserEmail() { return properties.getProperty("test.user.email"); }
    public static String getTestUserPassword() { return properties.getProperty("test.user.password"); }
    public static String getTestUserCompanyId() { return properties.getProperty("test.user.company.id"); }
    public static String getEmployeeDivisionId() { return properties.getProperty("employee.division.id"); }
}