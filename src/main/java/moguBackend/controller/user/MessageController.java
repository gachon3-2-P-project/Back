package moguBackend.controller.user;

import lombok.RequiredArgsConstructor;
import moguBackend.domain.dto.MessageDto;
import moguBackend.service.user.MessageService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("message")
public class MessageController {

    private final MessageService messageService;

    /**
     * 쪽지 등록
     */

    @PostMapping("/create")
    public ResponseEntity<MessageDto.MessageResponseDto> createMessage(@RequestParam("userId") Long userId, @RequestBody MessageDto.MessageRequestDto messageRequestDto) {
        MessageDto.MessageResponseDto responseDto = messageService.createMessage(userId, messageRequestDto);
        return ResponseEntity.ok().body(responseDto);
    }


    /**
     * 쪽지 고유 id로 쪽지 조회
     */
    @GetMapping("/get")
    public ResponseEntity<?> getMessage(@RequestParam("messageId") Long messageId) {
        MessageDto.MessageResponseDto Message = messageService.getMessage(messageId);
        return ResponseEntity.ok().body(Message);
    }

    /**
     * userId로 작성한 쪽지들 조회
     */
    @GetMapping("/getByUser")
    public ResponseEntity<List<MessageDto.MessageResponseDto>> getMessagesByUser(@RequestParam("userId") Long userId) {
        List<MessageDto.MessageResponseDto> messages = messageService.getMessagesByUser(userId);
        return ResponseEntity.ok().body(messages);
    }



    /**
     * 게시글Id로 작성된 쪽지 조회
     */





}
