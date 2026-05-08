package api.models.login;

public class LoginVariables {
    private String usernameOrEmail;
    private String password;
    private String companyId;

    public LoginVariables(String usernameOrEmail, String password, String companyId) {
        this.usernameOrEmail = usernameOrEmail;
        this.password = password;
        this.companyId = companyId;
    }

    // Getters and Setters (generate via IDE)
    public String getUsernameOrEmail() { return usernameOrEmail; }
    public void setUsernameOrEmail(String usernameOrEmail) { this.usernameOrEmail = usernameOrEmail; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public String getCompanyId() { return companyId; }
    public void setCompanyId(String companyId) { this.companyId = companyId; }
}