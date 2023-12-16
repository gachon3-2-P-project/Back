package moguBackend.config.spring_security.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import moguBackend.config.spring_security.PersonEntity;
import moguBackend.config.spring_security.auth.PrincipalDetails;
import moguBackend.repository.admin.AdminRepository;
import moguBackend.repository.user.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import java.io.IOException;

@Slf4j
public class JwtAuthorizationFilter extends BasicAuthenticationFilter {


    private AdminRepository adminRepository;
    private UserRepository userRepository;

    public JwtAuthorizationFilter(AuthenticationManager authenticationManager, UserRepository userRepository, AdminRepository adminRepository) {
        super(authenticationManager);
        this.userRepository = userRepository;
        this.adminRepository = adminRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        log.info("인가 필터 진입!!!");

        //JWT 토큰 검증
        String header = request.getHeader(JwtProperties.headerString);
        if (header == null || !header.startsWith(JwtProperties.tokenPrefix)) {
            log.info("토큰이 없음!! doFilter 수행");
            chain.doFilter(request, response);
            return;
        }
        System.out.println("header : " + header);
        String token = request.getHeader(JwtProperties.headerString)
                .replace(JwtProperties.tokenPrefix, "");

        String username = JWT.require(Algorithm.HMAC512(JwtProperties.secret)).build().verify(token)
                .getClaim("username").asString();


        if (username != null) {
            PersonEntity person;
            log.info(username);
            if (username.contains("admin_"))
                person = adminRepository.findByUsername(username);
            else person = userRepository.findByUsername(username);

            System.out.println("======");
            System.out.println("=====" + person.getUsername());


            PrincipalDetails principalDetails = new PrincipalDetails(person);
            System.out.println(person.getUsername());
            Authentication authentication = new UsernamePasswordAuthenticationToken(
                    principalDetails,
                    null,
                    principalDetails.getAuthorities());

            // 강제로 시큐리티의 세션에 접근하여 값 저장
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        chain.doFilter(request, response);

    }
}

