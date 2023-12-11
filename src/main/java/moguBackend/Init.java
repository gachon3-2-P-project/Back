package moguBackend;

import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import moguBackend.common.Role;
import moguBackend.domain.entity.AdminEntity;
import moguBackend.domain.entity.UserEntity;
import moguBackend.repository.admin.AdminRepository;
import moguBackend.repository.user.UserRepository;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class Init {

    private final UserRepository userRepository;
    private final AdminRepository adminRepository;

    @PostConstruct
    private void initFirst(){
        initAdmins();
        initUsers();
    }

    @Transactional
    public void initAdmins() {

        AdminEntity admin = new AdminEntity();
        admin.setUsername("admin_0"); //관리자아이디
        admin.setPassword("mogumogu"); //관리자 비밀번호
        admin.setRole(Role.ADMIN);
        adminRepository.save(admin);


    }

    @Transactional
    public void initUsers() {
        for (int i = 0; i < 5; i++) {
            UserEntity user = new UserEntity();
            user.setUsername("gachon" + (i+1) + "@gachon.ac.kr");
            user.setNickName("userNickname" + (i+1));
            user.setPassword("userPs" + (i+1));
            user.setRole(Role.USER);
            userRepository.save(user);
        }
    }


}
