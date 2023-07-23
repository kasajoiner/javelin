package javelin.service;

import javelin.entity.Employee;
import javelin.model.EmployeeRequest;
import javelin.repo.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmployeeService {

    private final EmployeeRepository repository;

    public Employee save(EmployeeRequest r) {
        var employee = repository.findById(r.id())
            .orElseGet(() -> {
                var e = new Employee();
                e.setId(r.id());
                return e;
            });
        if (r.role() != null) {
            employee.setRole(Employee.Role.valueOf(r.role().toUpperCase()));
        }
        if (r.status() != null) {
            employee.setStatus(Employee.Status.valueOf(r.status().toUpperCase()));
        }
        log.info("new employee {}", employee);
        return repository.save(employee);
    }

    public List<Employee> findAll() {
        return repository.findAll();
    }

    public List<Employee> findByRole(Employee.Role role) {
        return repository.findAllByRole(role);
    }

    public void deleteById(Long id) {
        repository.deleteById(id);
    }

}
