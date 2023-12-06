package moguBackend.dto.user;
import lombok.*;
import moguBackend.constant.Status;


public class TransactionDto {

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class TransactionResponseDto {

        private Long id;

        private Long articleId; //해당 거래 게시물 id

        private Long userId; //게시물 작성한 사용자 id

        private Status approvalStatus;

        private Status completionStatus;




    }
}
