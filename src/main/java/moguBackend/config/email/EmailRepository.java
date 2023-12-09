package moguBackend.config.email;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface EmailRepository extends JpaRepository<EmailAuthEntity, Long> {

    EmailAuthEntity findByEmail(String email);

    @Modifying
    @Query("DELETE FROM EmailAuthEntity e WHERE e.email = :email")
    void deleteByEmail(@Param("email") String email);



}
