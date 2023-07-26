package javelin.repo;

import javelin.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    Optional<Employee> findByIdAndRole(Long id, Employee.Role role);
    List<Employee> findAllByRole(Employee.Role role);
}
