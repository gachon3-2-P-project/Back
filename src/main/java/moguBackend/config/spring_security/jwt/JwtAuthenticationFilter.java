package moguBackend.config.spring_security.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import moguBackend.config.spring_security.auth.PrincipalDetails;
import moguBackend.config.spring_security.dto.LoginRequestDto;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;

    // 인증 요청시에 실행되는 함수 => /login
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {

        log.info("JwtAuthenticationFilter : 로그인 시도중");

        ObjectMapper om = new ObjectMapper();
        LoginRequestDto loginRequestDto = null;
        try {

            loginRequestDto = om.readValue(request.getInputStream(), LoginRequestDto.class);
            System.out.println(loginRequestDto);

        } catch (IOException e) {
            e.printStackTrace();
        }

        // 유저네임패스워드 토큰 생성
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(
                        loginRequestDto.getUsername(),
                        loginRequestDto.getPassword());

        System.out.println(authenticationToken);

        Authentication authentication =
                authenticationManager.authenticate(authenticationToken);

        PrincipalDetails principalDetailis = (PrincipalDetails) authentication.getPrincipal();
        System.out.println("로그인 완료됨: " + principalDetailis.getPerson().getPassword());
        System.out.println("Authentication : "+principalDetailis.getPerson().getUsername());


        return authentication;
    }

    //JWT 토큰을 만들어서 request 요청한 사용자에게 JWT토큰을 response
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
                                            Authentication authResult) throws IOException, ServletException {

        PrincipalDetails principalDetailis = (PrincipalDetails) authResult.getPrincipal();

        String jwtToken = JWT.create()
                .withSubject("mogumogu toekn")
                .withExpiresAt(new Date(System.currentTimeMillis()+ JwtProperties.expirationTime))
                .withClaim("id", principalDetailis.getPerson().getId())
                .withClaim("username", principalDetailis.getPerson().getUsername())
                .sign(Algorithm.HMAC512(JwtProperties.secret));

        response.addHeader(JwtProperties.headerString, jwtToken);

        // 바디에도 토큰 정보 추가
        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("username", principalDetailis.getPerson().getUsername());
        responseBody.put("id", principalDetailis.getPerson().getId());

        response.getWriter().write(new ObjectMapper().writeValueAsString(responseBody));
        response.getWriter().flush();
    }
}
