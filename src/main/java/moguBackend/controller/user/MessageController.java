package moguBackend.controller.user;

import lombok.RequiredArgsConstructor;
import moguBackend.dto.user.MessageDto;
import moguBackend.service.user.MessageService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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



}
