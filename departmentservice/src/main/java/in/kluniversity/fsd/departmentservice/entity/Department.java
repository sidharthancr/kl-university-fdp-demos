package in.kluniversity.fsd.departmentservice.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.NotBlank;

/**
 * A university department (e.g. Computer Science & Engineering, code "CSE").
 * Employees in the employee-service link here via their {@code departmentId}.
 */
@Entity
@Table(name = "departments", uniqueConstraints = {
        @UniqueConstraint(name = "uk_department_code", columnNames = "code"),
        @UniqueConstraint(name = "uk_department_name", columnNames = "name")
})
public class Department {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "name is required")
    @Column(nullable = false, unique = true)
    private String name;

    @NotBlank(message = "code is required")
    @Column(nullable = false, unique = true)
    private String code;

    /** Head of Department. */
    private String hod;

    /** Building / block where the department is located. */
    private String building;

    public Department() {
    }

    public Department(String name, String code, String hod, String building) {
        this.name = name;
        this.code = code;
        this.hod = hod;
        this.building = building;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getHod() {
        return hod;
    }

    public void setHod(String hod) {
        this.hod = hod;
    }

    public String getBuilding() {
        return building;
    }

    public void setBuilding(String building) {
        this.building = building;
    }
}
