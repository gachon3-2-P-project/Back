package moguBackend.domain.user;

import jakarta.persistence.*;
import lombok.*;
import moguBackend.TimeStamp;
import moguBackend.config.spring_security.PersonEntity;
import moguBackend.constant.Role;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserEntity extends TimeStamp implements PersonEntity {

    @Id
    @Column(name = "user_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String username; //사용자 아이디 (= 사용자 이메일)

    private String password;

    @Enumerated(EnumType.STRING)
    private Role role = Role.USER;

    private String nickName;

    private String emailCode; //인증 이메일 코드




}
