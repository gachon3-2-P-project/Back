package moguBackend.config.spring_security.auth;

import lombok.RequiredArgsConstructor;
import moguBackend.domain.admin.AdminEntity;
import moguBackend.domain.user.UserEntity;
import moguBackend.repository.admin.AdminRepository;
import moguBackend.repository.user.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PrincipalDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    private final AdminRepository adminRepository;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        System.out.println("PrincipalDetailsService : 진입");
        System.out.println(username);

        if (username.contains("admin_")) {
            AdminEntity adminEntity = adminRepository.findByUsername(username);

            return new PrincipalDetails(adminEntity);
        } else {
            UserEntity userEntity = userRepository.findByUsername(username);



            return new PrincipalDetails(userEntity);
        }
    }
}
