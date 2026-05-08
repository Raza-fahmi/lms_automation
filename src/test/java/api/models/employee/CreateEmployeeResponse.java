package api.models.employee;

public class CreateEmployeeResponse {
    private CreateEmployeeData createEmployee;

    public CreateEmployeeData getCreateEmployee() { return createEmployee; }
    public void setCreateEmployee(CreateEmployeeData createEmployee) { this.createEmployee = createEmployee; }

    public static class CreateEmployeeData {
        private String id;
        private String __typename;

        public String getId() { return id; }
        public void setId(String id) { this.id = id; }
        public String getTypename() { return __typename; }
        public void setTypename(String __typename) { this.__typename = __typename; }
    }
}