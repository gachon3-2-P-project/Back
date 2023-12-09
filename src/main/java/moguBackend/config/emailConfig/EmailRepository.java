package moguBackend.config.emailConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmailRepository extends JpaRepository<EmailAuthEntity, Long> {

    EmailAuthEntity findByEmail(String email);

    @Modifying
    @Query("DELETE FROM EmailAuthEntity e WHERE e.email = :email")
    void deleteByEmail(@Param("email") String email);

    List<EmailAuthEntity> findByAuthCodeExpirationMillisLessThan(long currentTimeMillis);



}
