package moguBackend.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import moguBackend.config.spring_security.PersonEntity;
import moguBackend.common.Role;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdminEntity implements PersonEntity {

    @Id
    @Column(name = "admin_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String username; //관리자 아이디

    private String password;

    @Enumerated(EnumType.STRING)
    private final Role role = Role.ADMIN;
}
