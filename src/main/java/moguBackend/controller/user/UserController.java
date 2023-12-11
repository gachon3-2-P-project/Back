package moguBackend.controller.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import moguBackend.domain.dto.ArticleDto;
import moguBackend.domain.dto.UserDto;
import moguBackend.service.user.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
@Slf4j
public class UserController {

    private final UserService userService;


    /**
     * 유저 고유 id로 조회
     */
    @GetMapping("/getUserArticles")
    public ResponseEntity<?> getUserArticles(@RequestParam("userId") Long userId) {
        List<ArticleDto.ArticleResponseDto> articles = userService.getUserArticles(userId);
        return ResponseEntity.ok().body(articles);
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
