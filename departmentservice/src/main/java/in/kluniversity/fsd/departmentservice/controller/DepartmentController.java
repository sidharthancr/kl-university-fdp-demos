package in.kluniversity.fsd.departmentservice.controller;

import in.kluniversity.fsd.departmentservice.dto.DepartmentPatchRequest;
import in.kluniversity.fsd.departmentservice.entity.Department;
import in.kluniversity.fsd.departmentservice.service.DepartmentService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/** REST API for departments. */
@RestController
@RequestMapping("/api/departments")
public class DepartmentController {

    private final DepartmentService service;

    public DepartmentController(DepartmentService service) {
        this.service = service;
    }

    @GetMapping
    public List<Department> all() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public Department byId(@PathVariable Long id) {
        return service.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Department create(@Valid @RequestBody Department department) {
        return service.create(department);
    }

    @PutMapping("/{id}")
    public Department update(@PathVariable Long id, @Valid @RequestBody Department department) {
        return service.update(id, department);
    }

    @PatchMapping("/{id}")
    public Department patch(@PathVariable Long id, @RequestBody DepartmentPatchRequest patch) {
        return service.patch(id, patch);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}
