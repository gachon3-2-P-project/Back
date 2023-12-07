package moguBackend.config.email;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmailRepository extends JpaRepository<EmailAuth, Long> {

    EmailAuth findByEmail(String email);

    void deleteByEmail(String email);

}
