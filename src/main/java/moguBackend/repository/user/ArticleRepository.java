package moguBackend.repository.user;

import moguBackend.domain.admin.AdminEntity;
import moguBackend.domain.user.ArticleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ArticleRepository extends JpaRepository<ArticleEntity, Long> {
}
