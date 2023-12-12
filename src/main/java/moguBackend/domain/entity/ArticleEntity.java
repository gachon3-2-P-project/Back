package moguBackend.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import moguBackend.common.TimeStamp;
import moguBackend.common.Transaction;

import java.util.ArrayList;
import java.util.List;


@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ArticleEntity extends TimeStamp {

    @Id
    @Column(name = "article_id")
//    @GeneratedValue(strategy = GenerationType.AUTO)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserEntity user;

    private String title; //제목

    @Lob
    @Column(columnDefinition = "TEXT")
    private String content; //내용

    private Integer numberOfPeople; //인원 수

    private Integer complain; // 게시글 신고 횟수 (최대 10회)

    @OneToMany(mappedBy = "article", cascade = CascadeType.ALL)
    //@JsonBackReference
    private List<MessageEntity> messages = new ArrayList<>();

    private String productName; //상품명

    private Integer cost; //금액

    @Enumerated(EnumType.STRING)
    private Transaction transactionStatus = Transaction.RECRUITOPEN;



}
