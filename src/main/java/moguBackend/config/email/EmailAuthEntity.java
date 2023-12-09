package moguBackend.config.email;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmailAuthEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;

    private String authCode;

    private Long authCodeExpirationMillis; //인증 코드 만료 시간


    @Transient
    public long getRemainingTimeMillis() {
        long currentTimeMillis = System.currentTimeMillis();
        long remainingTimeMillis = authCodeExpirationMillis - currentTimeMillis;
        return Math.max(remainingTimeMillis, 0);
    }


}
