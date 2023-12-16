package moguBackend.repository.user;
import moguBackend.domain.dto.ArticleDto;
import moguBackend.domain.entity.ArticleEntity;
import moguBackend.domain.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArticleRepository extends JpaRepository<ArticleEntity, Long> {

    @Query("SELECT a FROM ArticleEntity a WHERE a.title LIKE %:keyword% ")
    List<ArticleEntity> findKeyword(String keyword);

    @Query("SELECT a FROM ArticleEntity a ORDER BY a.id DESC")
    List<ArticleEntity> getAllArticle();

    List<ArticleEntity> findByUser(UserEntity user);


}
