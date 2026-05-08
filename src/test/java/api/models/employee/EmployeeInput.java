package api.models.employee;

import com.google.gson.annotations.SerializedName;

public class EmployeeInput {
    private String name;
    private String employeeId;
    private String email;
    private String phoneNumber;
    private String divisionId;
    private String employeeRole;
    private String gender;
    private String address;
    private String nik;
    private String npwp;

    // Constructor
    public EmployeeInput() {}

    public EmployeeInput(String name, String employeeId, String email, String phoneNumber,
                         String divisionId, String employeeRole, String gender,
                         String address, String nik, String npwp) {
        this.name = name;
        this.employeeId = employeeId;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.divisionId = divisionId;
        this.employeeRole = employeeRole;
        this.gender = gender;
        this.address = address;
        this.nik = nik;
        this.npwp = npwp;
    }

    // Getters and Setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmployeeId() { return employeeId; }
    public void setEmployeeId(String employeeId) { this.employeeId = employeeId; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

    public String getDivisionId() { return divisionId; }
    public void setDivisionId(String divisionId) { this.divisionId = divisionId; }

    public String getEmployeeRole() { return employeeRole; }
    public void setEmployeeRole(String employeeRole) { this.employeeRole = employeeRole; }

    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getNik() { return nik; }
    public void setNik(String nik) { this.nik = nik; }

    public String getNpwp() { return npwp; }
    public void setNpwp(String npwp) { this.npwp = npwp; }

    // Builder pattern untuk memudahkan pembuatan object
    public static class Builder {
        private EmployeeInput employee = new EmployeeInput();

        public Builder name(String name) { employee.name = name; return this; }
        public Builder employeeId(String employeeId) { employee.employeeId = employeeId; return this; }
        public Builder email(String email) { employee.email = email; return this; }
        public Builder phoneNumber(String phoneNumber) { employee.phoneNumber = phoneNumber; return this; }
        public Builder divisionId(String divisionId) { employee.divisionId = divisionId; return this; }
        public Builder employeeRole(String employeeRole) { employee.employeeRole = employeeRole; return this; }
        public Builder gender(String gender) { employee.gender = gender; return this; }
        public Builder address(String address) { employee.address = address; return this; }
        public Builder nik(String nik) { employee.nik = nik; return this; }
        public Builder npwp(String npwp) { employee.npwp = npwp; return this; }

        public EmployeeInput build() { return employee; }
    }
}