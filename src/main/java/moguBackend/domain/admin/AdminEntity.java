package moguBackend.domain.admin;

import jakarta.persistence.*;
import lombok.*;
import moguBackend.constant.Role;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdminEntity {

    @Id
    @Column(name = "admin_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String adminName; //관리자 아이디

    private String password;

    @Enumerated(EnumType.STRING)
    private final Role role = Role.ADMIN;
}
