package api.models.employee;

public class UpdateEmployeeResponse {
    private UpdateEmployeeData updateEmployee;

    public UpdateEmployeeData getUpdateEmployee() { return updateEmployee; }
    public void setUpdateEmployee(UpdateEmployeeData updateEmployee) { this.updateEmployee = updateEmployee; }

    public static class UpdateEmployeeData {
        private String id;
        private String __typename;

        public String getId() { return id; }
        public void setId(String id) { this.id = id; }
        public String getTypename() { return __typename; }
        public void setTypename(String __typename) { this.__typename = __typename; }
    }
}