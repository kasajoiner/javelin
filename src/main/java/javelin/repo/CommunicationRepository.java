package javelin.repo;

import javelin.entity.Communication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CommunicationRepository extends JpaRepository<Communication, Long> {

    Optional<Communication> findByObjectId(String objectId);
}
