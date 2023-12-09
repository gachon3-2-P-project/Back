package moguBackend.controller.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import moguBackend.domain.dto.UserDto;
import moguBackend.service.user.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class JoinController {

    private final UserService userService;

    /**
     * 회원 가입
     */
    @PostMapping(value = "/join")
    public ResponseEntity<?> createUser(@RequestBody UserDto.UserRequestDto userRequestDto) {
        //로그
        log.info("createMember 진입");
        log.info("userRequestDto의 username : " + userRequestDto.getUsername());
        UserDto.UserResponseDto user = userService.createUser(userRequestDto);
        log.info("userResponseDto의 username : " + user.getUsername());
        return ResponseEntity.ok().body(user);
    }
}
