package in.kluniversity.fsd.departmentservice.service;

import in.kluniversity.fsd.departmentservice.dto.DepartmentPatchRequest;
import in.kluniversity.fsd.departmentservice.entity.Department;
import in.kluniversity.fsd.departmentservice.exception.DepartmentNotFoundException;
import in.kluniversity.fsd.departmentservice.repository.DepartmentRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/** Business logic for departments. Controllers stay thin; rules live here. */
@Service
public class DepartmentService {

    private final DepartmentRepository repository;

    public DepartmentService(DepartmentRepository repository) {
        this.repository = repository;
    }

    public List<Department> findAll() {
        return repository.findAll();
    }

    public Department findById(Long id) {
        return repository.findById(id).orElseThrow(() -> new DepartmentNotFoundException(id));
    }

    public Department create(Department department) {
        department.setId(null);
        return repository.save(department);
    }

    /** Full replace (PUT). */
    public Department update(Long id, Department incoming) {
        Department existing = findById(id);
        existing.setName(incoming.getName());
        existing.setCode(incoming.getCode());
        existing.setHod(incoming.getHod());
        existing.setBuilding(incoming.getBuilding());
        return repository.save(existing);
    }

    /** Partial update (PATCH) - only non-null fields change. */
    public Department patch(Long id, DepartmentPatchRequest patch) {
        Department existing = findById(id);
        if (patch.name() != null) {
            existing.setName(patch.name());
        }
        if (patch.code() != null) {
            existing.setCode(patch.code());
        }
        if (patch.hod() != null) {
            existing.setHod(patch.hod());
        }
        if (patch.building() != null) {
            existing.setBuilding(patch.building());
        }
        return repository.save(existing);
    }

    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new DepartmentNotFoundException(id);
        }
        repository.deleteById(id);
    }
}
