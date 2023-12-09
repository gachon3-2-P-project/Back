package moguBackend.domain.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

import java.time.LocalTime;
import java.util.List;

public class ArticleDto {

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class ArticleRequestDto {


        @NotBlank
        private String title; //제목

        private String content; //내용

        private Integer numberOfPeople; //인원 수

        private Integer complain; // 게시글 신고 횟수 (최대 5회)


    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class ArticleResponseDto {

        private Long id;

        private Long userId; //게시물 작성한 사용자 id

        private String nickName;

        private String title; //제목

        private String content; //내용

        private Integer complain; // 게시글 신고 횟수 (최대 5회)

        private List<MessageDto.MessageResponseDto> messages;

        private Integer numberOfPeople; //인원 수

        private LocalTime createdAt;



    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class ArticlePatchDto {

        @NotEmpty(message = "Title은 필수값입니다.")
        private String title; //제목

        private String content; //내용

        private Integer numberOfPeople; //인원 수

        private Integer complain; // 게시글 신고 횟수 (최대 5회)

    }


}
