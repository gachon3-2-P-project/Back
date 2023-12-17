package moguBackend.controller.user;

import lombok.RequiredArgsConstructor;
import moguBackend.domain.dto.ArticleDto;
import moguBackend.domain.dto.MessageDto;
import moguBackend.exception.BusinessLogicException;
import moguBackend.service.user.ArticleService;
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


//    /**
//     * 쪽지 고유 id로 쪽지 조회
//     */
//    @GetMapping("/get")
//    public ResponseEntity<?> getMessage(@RequestParam("messageId") Long messageId) {
//        MessageDto.MessageResponseDto Message = messageService.getMessage(messageId);
//        return ResponseEntity.ok().body(Message);
//    }

//    /**
//     * userId로 작성한 쪽지들 조회
//     */
//    @GetMapping("/getByUser")
//    public ResponseEntity<List<MessageDto.MessageResponseDto>> getMessagesByUser(@RequestParam("userId") Long userId) {
//        List<MessageDto.MessageResponseDto> messages = messageService.getMessagesByUser(userId);
//        return ResponseEntity.ok().body(messages);
//    }



    /**
     * 게시글Id로 작성된 쪽지 조회
     */

    @GetMapping("/getAllArticleMessages")
    public ResponseEntity<List<MessageDto.MessageResponseDto>> getArticleMessages(@RequestParam Long articleId, Long userId) {
        List<MessageDto.MessageResponseDto> articlesWithMessages = messageService.getAllArticleMessages(articleId, userId);
        return ResponseEntity.ok().body(articlesWithMessages);
    }

    /**
     * 쪽지 삭제
     */
    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteMessage(@RequestParam Long messageId) {
        messageService.deleteMessage(messageId);
        return ResponseEntity.ok().body("Deleted Message Id : " + messageId);
    }

    /**
     * 해당 게시물 쪽지 조회
     */
    @GetMapping("/getMessageStorage")
    public ResponseEntity<List<MessageDto.MessageResponseDto>> getMessageStorage(@RequestParam Long userId) {

        List<MessageDto.MessageResponseDto> articleResponses = messageService.getMessageStorage(userId);
        return ResponseEntity.ok(articleResponses);

    }

    /**
     * 게시물 작성자가 해당 게시물에 작성한 메시지 조회
     */
    @GetMapping("/getArticleAuthorMessages")
    public ResponseEntity<List<MessageDto.MessageResponseDto>> getArticleAuthorMessages(@RequestParam Long articleId, Long receiverUserId) {

        List<MessageDto.MessageResponseDto> messageResponses = messageService.getArticleAuthorMessages(articleId, receiverUserId);
        return ResponseEntity.ok(messageResponses);

    }

    /**
     * 사용자가 해당 게시물에 작성한 메시지 조회
     */
    @GetMapping("/getArticleSenderMessages")
    public ResponseEntity<List<MessageDto.MessageResponseDto>> getArticleSenderMessages(@RequestParam Long articleId, Long userId) {

        List<MessageDto.MessageResponseDto> messageResponses = messageService.getArticleSenderMessages(articleId, userId);
        return ResponseEntity.ok(messageResponses);

    }





}
