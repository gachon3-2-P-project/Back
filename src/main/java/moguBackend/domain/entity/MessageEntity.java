package moguBackend.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import moguBackend.common.TimeStamp;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MessageEntity extends TimeStamp{

    @Id
    @Column(name = "message_id")
//    @GeneratedValue(strategy = GenerationType.AUTO)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "article_id")
    private ArticleEntity article;

    private String receiver; //쪽지 수신자 닉네임

    @Lob
    @Column(columnDefinition = "TEXT")
    private String content; //내용
}
