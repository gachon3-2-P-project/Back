package moguBackend.repository.user;

import moguBackend.config.email.EmailAuthEntity;
import moguBackend.domain.admin.AdminEntity;
import moguBackend.domain.user.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {

    @Query("SELECT m FROM UserEntity m")
    List<UserEntity> getAllUser();

    boolean existsByNickName(String nickName);

    UserEntity findByUsername(String username);

    @Modifying
    @Query("DELETE FROM UserEntity u WHERE u.username = :username")
    void deleteByEmail(@Param("username") String username);


}


