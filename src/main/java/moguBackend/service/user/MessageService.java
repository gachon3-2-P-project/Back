package moguBackend.service.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import moguBackend.domain.entity.ArticleEntity;
import moguBackend.domain.entity.MessageEntity;
import moguBackend.domain.entity.UserEntity;
import moguBackend.domain.dto.MessageDto;
import moguBackend.exception.BusinessLogicException;
import moguBackend.exception.ExceptionCode;
import moguBackend.domain.mapper.ArticleMapper;
import moguBackend.domain.mapper.MessageMapper;
import moguBackend.repository.user.ArticleRepository;
import moguBackend.repository.user.MessageRepository;
import moguBackend.repository.user.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class MessageService {

    private final UserRepository userRepository;
    private final ArticleRepository articleRepository;
    private  final MessageRepository messageRepository;
    private final MessageMapper messageMapper;
    private final ArticleMapper articleMapper;

    /**
     * 쪽지 생성
     */
    @Transactional
    public MessageDto.MessageResponseDto createMessage(Long userId, MessageDto.MessageRequestDto messageRequestDto) {
        UserEntity userEntity = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessLogicException(ExceptionCode.USER_NOT_FOUND));

        ArticleEntity articleEntity = articleRepository.findById(messageRequestDto.getArticleId())
                .orElseThrow(() -> new BusinessLogicException(ExceptionCode.ARTICLE_NOT_EXIST));

        MessageEntity savedMessage = messageRepository.save(messageMapper.toRequestEntity(messageRequestDto, userEntity, articleEntity));
        MessageDto.MessageResponseDto responseDto = messageMapper.toResponseDto(savedMessage);
        responseDto.setUserId(userId);
        responseDto.setArticleId(messageRequestDto.getArticleId());

        return responseDto;
    }

//    @Transactional
//    public MessageDto.MessageResponseDto createMessage(Long userId, MessageDto.MessageRequestDto messageRequestDto) {
//        UserEntity sender = userRepository.findById(userId)
//                .orElseThrow(() -> new BusinessLogicException(ExceptionCode.USER_NOT_FOUND));
//
//        ArticleEntity articleEntity = articleRepository.findById(messageRequestDto.getArticleId())
//                .orElseThrow(() -> new BusinessLogicException(ExceptionCode.ARTICLE_NOT_EXIST));
//
//        String receiverNickname = messageRequestDto.getReceiver();
//        UserEntity receiver = userRepository.findByNickname(receiverNickname)
//                .orElseThrow(() -> new BusinessLogicException(ExceptionCode.USER_NOT_FOUND));
//
//        MessageEntity savedMessage = messageRepository.save(messageMapper.toRequestEntity(messageRequestDto, sender, receiver, articleEntity));
//
//        MessageDto.MessageResponseDto responseDto = messageMapper.toResponseDto(savedMessage);
//        responseDto.setUserId(userId);
//        responseDto.setArticleId(messageRequestDto.getArticleId());
//
//        return responseDto;
//    }


    /**
     * 쪽지 고유 아이디로 쪽지 조회
     */
    public MessageDto.MessageResponseDto getMessage(Long messageId) {

        return messageMapper.toResponseDto(messageRepository.findById(messageId)
                .orElseThrow(() -> new BusinessLogicException(ExceptionCode.Message_IS_NOT_EXIST)));

    }

    @Transactional
    public void deleteMessage(Long messageId) {
        messageRepository.deleteById(messageId);
        log.info("삭제된 Message: {}",messageId);
    }


    /**
     * userId로 쪽지 조회
     */
    public List<MessageDto.MessageResponseDto> getMessagesByUser(Long userId) {
        UserEntity userEntity = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessLogicException(ExceptionCode.USER_NOT_FOUND));

        List<MessageEntity> userMessages = messageRepository.findByUser(userEntity);

        return userMessages.stream()
                .map(messageMapper::toResponseDto)
                .collect(Collectors.toList());
    }






}
