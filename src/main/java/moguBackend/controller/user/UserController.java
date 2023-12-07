package moguBackend.controller.user;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import moguBackend.dto.user.ArticleDto;
import moguBackend.dto.user.UserDto;
import moguBackend.service.user.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("user")
@Slf4j
public class UserController {

    private final UserService userService;


    /**
     * 유저 고유 id로 조회
     */
    @GetMapping("/get")
    public ResponseEntity<?> getUser (@RequestParam("userId") Long userId) {

        UserDto.UserResponseDto user = userService.getUser(userId);
        return ResponseEntity.ok().body(user);
    }

    /**
     *
     * update -> 닉네임 수정
     */
    @PatchMapping("/update")
    public ResponseEntity<?> updateUser(@RequestParam("userId") Long userId, @RequestBody UserDto.UserPatchDto userPatchDto) {
        return ResponseEntity.ok().body(userService.updateUser(userId, userPatchDto));
    }

    /**
     * 사용자 탈퇴 기능
     */
    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteUser(@RequestParam("userId") long userId) {
        userService.deleteUser(userId);
        return ResponseEntity.ok().body("Deleted UserId: " + userId);
    }

    /**
     * 관리자 사용자 전체 조회
     */
    @GetMapping("/admin/getAll")
    public ResponseEntity<List<UserDto.UserResponseDto>> getAllUser() {
        List<UserDto.UserResponseDto> users = userService.getAllUser();
        return ResponseEntity.ok().body(users);
    }










}
