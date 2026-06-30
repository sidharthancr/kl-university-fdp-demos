package in.kluniversity.fsd.departmentservice.dto;

/**
 * Partial-update payload for PATCH. Any {@code null} field is left unchanged.
 * Records make this a concise, immutable carrier.
 */
public record DepartmentPatchRequest(String name, String code, String hod, String building) {
}
