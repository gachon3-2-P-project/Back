package moguBackend.repository.user;

import moguBackend.domain.admin.AdminEntity;
import moguBackend.domain.user.MessageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MessageRepository extends JpaRepository<MessageEntity, Long> {
}
