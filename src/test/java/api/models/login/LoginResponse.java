package api.models.login;

import java.util.List;

public class LoginResponse {
    private User user;
    private List<Error> errors;
    private String __typename;

    // Getters and Setters
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
    public List<Error> getErrors() { return errors; }
    public void setErrors(List<Error> errors) { this.errors = errors; }
    public String getTypename() { return __typename; }
    public void setTypename(String __typename) { this.__typename = __typename; }

    public static class User {
        private String username;
        private String id;
        private String role;
        private String __typename;

        // Getters and Setters
        public String getUsername() { return username; }
        public void setUsername(String username) { this.username = username; }
        public String getId() { return id; }
        public void setId(String id) { this.id = id; }
        public String getRole() { return role; }
        public void setRole(String role) { this.role = role; }
        public String getTypename() { return __typename; }
        public void setTypename(String __typename) { this.__typename = __typename; }
    }

    public static class Error {
        private String field;
        private String message;
        private String __typename;

        // Getters and Setters
        public String getField() { return field; }
        public void setField(String field) { this.field = field; }
        public String getMessage() { return message; }
        public void setMessage(String message) { this.message = message; }
        public String getTypename() { return __typename; }
        public void setTypename(String __typename) { this.__typename = __typename; }
    }
}