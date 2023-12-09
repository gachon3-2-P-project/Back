package moguBackend.repository.admin;

import moguBackend.domain.entity.AdminEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminRepository extends JpaRepository<AdminEntity, Long> {
    AdminEntity findByUsername(String username);

}
