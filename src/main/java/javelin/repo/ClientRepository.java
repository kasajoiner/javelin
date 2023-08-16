package javelin.repo;

import javelin.entity.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {
    Optional<Client> findByPhone(String phone);

    List<Client> findAllByStatus(Client.Status status);

    List<Client> findAllById(Long id);
}
