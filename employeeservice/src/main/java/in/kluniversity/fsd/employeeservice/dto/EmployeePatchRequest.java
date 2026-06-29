package in.kluniversity.fsd.employeeservice.dto;

import jakarta.validation.constraints.Email;

/**
 * Payload for a PATCH (partial update). Every field is optional:
 * only the fields the client actually sends (non-null) are applied to the
 * existing employee. Contrast with PUT, which replaces the whole record.
 */
public class EmployeePatchRequest {

    private String firstName;
    private String lastName;

    @Email(message = "email must be a valid address")
    private String email;

    private String jobTitle;
    private Long departmentId;

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public Long getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(Long departmentId) {
        this.departmentId = departmentId;
    }
}
