package moguBackend.dto.user;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

public class MessageDto {

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class MessageRequestDto {


        private String content; //내용

        private String receiver; // 수신자 닉네임


    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class MessageResponseDto {

        private Long id;

        private Long userId; //게시물 작성한 사용자 id

        private Long articleId; //해당 거래 게시물 id

        private String receiver; // 수신자 닉네임

        private String content; //내용

        private LocalDate createAt;



    }
}
