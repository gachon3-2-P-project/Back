package moguBackend.config.spring_security.jwt;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtProperties {

    protected static String secret;
    protected static Long expirationTime;
    protected static String tokenPrefix;
    protected static String headerString;

    @Value("${jwt.secret}")
    public void setSecret(String secret) {
        JwtProperties.secret = secret;
    }

    @Value("${jwt.expiration-time}") //10일
    public void setExpirationTime(Long expirationTime) {
        JwtProperties.expirationTime = expirationTime;
    }

    @Value("${jwt.token-prefix}")
    public void setTokenPrefix(String tokenPrefix) {
        JwtProperties.tokenPrefix = tokenPrefix+" ";
    }

    @Value("${jwt.header-string}")
    public void setHeaderString(String headerString) {
        JwtProperties.headerString = headerString;
    }
}
