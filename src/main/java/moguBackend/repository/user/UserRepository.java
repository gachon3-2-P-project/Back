package moguBackend.repository.user;

import moguBackend.domain.admin.AdminEntity;
import moguBackend.domain.user.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {

    @Query("SELECT m FROM UserEntity m")
    List<UserEntity> getAllUser();

    boolean existsByNickName(String nickName);

    UserEntity findByUsername(String username);
}
