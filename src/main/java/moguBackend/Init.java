package moguBackend;

import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import moguBackend.common.Role;
import moguBackend.domain.entity.AdminEntity;
import moguBackend.domain.entity.ArticleEntity;
import moguBackend.domain.entity.UserEntity;
import moguBackend.repository.admin.AdminRepository;
import moguBackend.repository.user.ArticleRepository;
import moguBackend.repository.user.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class Init {

    private final UserRepository userRepository;
    private final AdminRepository adminRepository;
    private final ArticleRepository articleRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @PostConstruct
    private void initFirst(){
        initAdmins();
        initUsers();
        initArticles();
    }

    @Transactional
    public void initAdmins() {

        AdminEntity admin = new AdminEntity();
        admin.setUsername("admin_0"); //관리자아이디
        admin.setPassword(bCryptPasswordEncoder.encode("admin_" + 0));
        admin.setRole(Role.ADMIN);
        adminRepository.save(admin);


    }

    @Transactional
    public void initUsers() {
        for (int i = 0; i < 5; i++) {
            UserEntity user = new UserEntity();
            user.setUsername("gachon" + (i+1) + "@gachon.ac.kr");
            user.setNickName("userNickname" + (i+1));
            user.setPassword(bCryptPasswordEncoder.encode("member" + i));
            user.setRole(Role.USER);
            userRepository.save(user);
        }
    }

        @Transactional
    public void initArticles() {
        List<UserEntity> user = userRepository.findAll();
        for (int i = 0; i < 5; i++) {
            ArticleEntity article = new ArticleEntity();
            article.setUser(user.get(i));
            article.setTitle("제목" + i);
            article.setContent("Content");
            article.setNumberOfPeople(3);
            article.setCost(10000 * i);
            article.setComplain(0);
            article.setDepositNumber(0);
            article.setTransactionNumber(0);
            articleRepository.save(article);
        }
    }


}
