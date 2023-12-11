package moguBackend.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import moguBackend.common.TimeStamp;
import moguBackend.config.spring_security.PersonEntity;
import moguBackend.common.Role;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserEntity extends TimeStamp implements PersonEntity {

    @Id
    @Column(name = "user_id")
//    @GeneratedValue(strategy = GenerationType.AUTO)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username; //사용자 아이디 (= 사용자 이메일)

    private String password;

    @Enumerated(EnumType.STRING)
    private Role role = Role.USER;

    private String nickName;




}
