package moguBackend.service.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import moguBackend.domain.dto.ArticleDto;
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

import java.util.ArrayList;
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

        // receiver의 닉네임이 DB에 등록된 사용자인지 확인
        if (!userRepository.existsByNickName(messageRequestDto.getReceiver())) {
            throw new BusinessLogicException(ExceptionCode.USER_NOT_FOUND);
        }

        MessageEntity savedMessage = messageRepository.save(messageMapper.toRequestEntity(messageRequestDto, userEntity, articleEntity));
        savedMessage.setSender(userEntity.getNickName());
        MessageDto.MessageResponseDto responseDto = messageMapper.toResponseDto(savedMessage);
        responseDto.setUserId(userId);
        responseDto.setArticleId(messageRequestDto.getArticleId());

        return responseDto;
    }



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

    /**
     * 해당 게시물 쪽지 조회
     */

    public List<ArticleDto.ArticleResponseDto> getArticleMessages(Long articleId) {

        ArticleEntity articleEntity = articleRepository.findById(articleId)
                .orElseThrow(() -> new BusinessLogicException(ExceptionCode.ARTICLE_NOT_EXIST));

        // 게시물과 연관된 메시지들
        List<MessageEntity> messages = articleEntity.getMessages();
        List<MessageDto.MessageResponseDto> messageDtos = new ArrayList<>();

        // 메시지들을 DTO로 변환하여 리스트에 추가
        for (MessageEntity messageEntity : messages) {
            MessageDto.MessageResponseDto messageResponseDto = messageMapper.toResponseDto(messageEntity);

            messageResponseDto.setUserId(messageEntity.getUser().getId());
            //messageResponseDto.setNickName(messageEntity.getUser().getNickName());

            messageDtos.add(messageResponseDto);
        }

        List<ArticleDto.ArticleResponseDto> result = new ArrayList<>();
        ArticleDto.ArticleResponseDto articleResponseDto = articleMapper.toResponseDto(articleEntity);
        articleResponseDto.setMessages(messageDtos);
        result.add(articleResponseDto);

        return result;
    }

    /**
     * 쪽지함 구현
     */
    public List<ArticleDto.ArticleResponseDto> getMessageStorage(Long userId) {

        UserEntity userEntity = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessLogicException(ExceptionCode.USER_NOT_FOUND));

        List<MessageEntity> messages = messageRepository.findByReceiver(userEntity.getNickName());

        List<ArticleDto.ArticleResponseDto> articleResponses = new ArrayList<>();

        for (MessageEntity messageEntity : messages) {
            if (messageEntity.getSender().equals(userEntity.getNickName())) {

                ArticleEntity articleEntity = articleRepository.findById(messageEntity.getArticle().getId())
                        .orElseThrow(() -> new BusinessLogicException(ExceptionCode.ARTICLE_NOT_EXIST));
                ArticleDto.ArticleResponseDto articleResponse = articleMapper.toResponseDto(articleEntity);
                articleResponses.add(articleResponse);
            }
        }




        return articleResponses;
    }




}
