package moguBackend.dto.admin;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;

public class AdminDto {

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class AdminRequestDto {

        @NotBlank
        @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*[!@#$%^*+=-])(?=.*[0-9]).{8,15}$",
                message = "비밀번호는 8~15자 영문, 숫자, 특수문자 조합이어야 합니다.")
        private String password;

        @NotBlank
        private String username; //로그인 아이디


    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class AdminResponseDto {
        private Long id;

        private String username; //로그인 아이디

    }
}
