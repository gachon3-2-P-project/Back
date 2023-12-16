package moguBackend.domain.entity;
import jakarta.persistence.*;
import lombok.*;
import moguBackend.common.Status;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransactionEntity {

    @Id
    @Column(name = "transaction_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "article_id")
    private ArticleEntity article;

    @Enumerated(EnumType.STRING)
    private Status approvalStatus; //거래 승인 상태

    @Enumerated(EnumType.STRING)
    private Status completionStatus; //거래 완료 상태




}
