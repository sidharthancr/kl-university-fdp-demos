package in.kluniversity.fsd.departmentservice.exception;

/** Thrown when a department id does not exist. Mapped to HTTP 404. */
public class DepartmentNotFoundException extends RuntimeException {

    public DepartmentNotFoundException(Long id) {
        super("Department not found: " + id);
    }
}
