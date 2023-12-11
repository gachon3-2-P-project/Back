package moguBackend.config.emailConfig;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import moguBackend.domain.dto.UserDto;
import moguBackend.service.user.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Slf4j
public class EmailController {

    private final UserService userService;
    private final EmailRepository emailRepository;

    /**
     * 회원 가입
     */
    @PostMapping(value = "/join")
    public ResponseEntity<?> createUser(@RequestBody UserDto.UserRequestDto userRequestDto) {
        //로그

        String email = userRequestDto.getUsername();
        userService.sendCodeToEmail(email);
        log.info("createMember 진입");
        log.info("userRequestDto의 username : " + userRequestDto.getUsername());
        UserDto.UserResponseDto user = userService.createUser(userRequestDto);
        log.info("userResponseDto의 username : " + user.getUsername());

        return ResponseEntity.ok().body(email + "로 메일이 전송되었습니다.");
    }

//    /**
//     * 가천대 이메일 인증 코드 전송
//     */
//    @PostMapping("/emails/verification-requests")
//    public ResponseEntity<?> sendMessage(@RequestParam("email") String email) {
//
//        log.info("----");
//        userService.sendCodeToEmail(email);
//
//        return ResponseEntity.ok().body(email + "로 메일이 전송되었습니다.");
//    }

    @GetMapping("/emails/verifications")
    @Transactional
    public ResponseEntity<?> verificationEmail(@RequestParam("email") String email,
                                               @RequestParam("code") String authCode) {
        boolean verificationResult = userService.verifiedCode(email, authCode);

        if (verificationResult) {
            // 이메일 인증 성공 시 EmailAuth테이블의 해당 데이터 삭제 -> user테이블에는 데이터 존재
            emailRepository.deleteByEmail(email);
            return ResponseEntity.ok().body(email + " 이메일이 성공적으로 인증되었습니다.");
        } else {
            return ResponseEntity.badRequest().body("이메일 인증에 실패했습니다. 올바른 인증 코드를 입력하세요.");
        }
    }
}
