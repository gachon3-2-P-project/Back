package moguBackend.service.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import moguBackend.domain.user.ArticleEntity;
import moguBackend.domain.user.MessageEntity;
import moguBackend.domain.user.UserEntity;
import moguBackend.dto.user.ArticleDto;
import moguBackend.dto.user.MessageDto;
import moguBackend.exception.BusinessLogicException;
import moguBackend.exception.ExceptionCode;
import moguBackend.mapper.user.ArticleMapper;
import moguBackend.mapper.user.MessageMapper;
import moguBackend.repository.user.ArticleRepository;
import moguBackend.repository.user.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class ArticleService {

    private final ArticleRepository articleRepository;
    private final ArticleMapper articleMapper;
    private final UserRepository userRepository;
    private final MessageMapper messageMapper;

    /**
     * 게시물 등록
     */
    @Transactional
    public ArticleDto.ArticleResponseDto createArticle(Long userId, ArticleDto.ArticleRequestDto articleRequestDto) {

        UserEntity userEntity = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessLogicException(ExceptionCode.USER_NOT_FOUND));


        ArticleEntity savedArticle = articleRepository.save(articleMapper.toReqeustEntity(articleRequestDto, userEntity));

        ArticleDto.ArticleResponseDto responseDto = articleMapper.toResponseDto(savedArticle);
        responseDto.setUserId(userId);
        responseDto.setComplain(0); //게시물 등록시 신고 횟수 0으로 초기화

        return responseDto;
    }

    /**
     * 게시물 조회
     */

    public ArticleDto.ArticleResponseDto getArticle(Long articleId) {

        ArticleEntity articleEntity = articleRepository.findById(articleId)
                .orElseThrow(() -> new BusinessLogicException(ExceptionCode.ARTICLE_NOT_EXIST));

        return articleMapper.toResponseDto(articleEntity);

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
            messageResponseDto.setNickName(messageEntity.getUser().getNickName());

            messageDtos.add(messageResponseDto);
        }

        List<ArticleDto.ArticleResponseDto> result = new ArrayList<>();
        ArticleDto.ArticleResponseDto articleResponseDto = articleMapper.toResponseDto(articleEntity);
        articleResponseDto.setMessages(messageDtos);
        result.add(articleResponseDto);

        return result;
    }






    /**
     * 전체 게시물 조회
     */

    @Transactional
    public List<ArticleDto.ArticleResponseDto> getAllArticle() {
        List<ArticleEntity> articleEntities = articleRepository.getAllArticle();
        List<ArticleDto.ArticleResponseDto> articleResponseDtos = new ArrayList<>();

        for (ArticleEntity articleEntity : articleEntities) {
            ArticleDto.ArticleResponseDto articleResponseDto = articleMapper.toResponseDto(articleEntity);


            //게시물에 관련된 쪽지도 같이 조회하는 로직
            List<MessageEntity> Messages = articleEntity.getMessages();
            List<MessageDto.MessageResponseDto> MessageDtos = new ArrayList<>();
            for (MessageEntity MessageEntity : Messages) {
                MessageDto.MessageResponseDto messageResponseDto = messageMapper.toResponseDto(MessageEntity);


                messageResponseDto.setUserId(MessageEntity.getUser().getId());
                messageResponseDto.setNickName(MessageEntity.getUser().getNickName());

                MessageDtos.add(messageResponseDto);
            }

            articleResponseDto.setMessages(MessageDtos);
            articleResponseDtos.add(articleResponseDto);
        }

        return articleResponseDtos;
    }

    /**
     * 게시물 삭제
     */
    @Transactional
    public void deleteArticle(Long articleId) {
        articleRepository.deleteById(articleId);
        log.info("삭제된 Article: {}",articleId);
    }

    /**
     * 게시물 키워드 검색
     */

    @Transactional
    public List<ArticleDto.ArticleResponseDto> searchArticle(String keyword) {

        String processedKeyword = keyword.replaceAll("\\s+","");

        List<ArticleEntity> articleEntities = articleRepository.findKeyword(processedKeyword);
        List<ArticleDto.ArticleResponseDto> articleResponseDtos = new ArrayList<>();

        if (articleEntities.isEmpty()) {
            throw new BusinessLogicException(ExceptionCode.KEYWORD_IS_NOT_EXIST);
        }


        for (ArticleEntity ArticleEntity :articleEntities){
            ArticleDto.ArticleResponseDto articleResponseDto = articleMapper.toResponseDto(ArticleEntity);
            articleResponseDtos.add(articleResponseDto);
        }

        return articleResponseDtos;
    }

    /**
     * 게시물 수정
     */
    @Transactional
    public ArticleDto.ArticleResponseDto updateArticle(Long ArticleId, ArticleDto.ArticlePatchDto articlePatchDto) {

        ArticleEntity articleEntity = articleRepository.findById(ArticleId)
                .orElseThrow(() -> new BusinessLogicException(ExceptionCode.ARTICLE_NOT_EXIST));

        articleMapper.updateFromPatchDto(articlePatchDto,articleEntity);

        return articleMapper.toResponseDto(articleEntity);
    }

    /**
     * 게시물 신고
     */
    @Transactional
    public ArticleDto.ArticleResponseDto complainArticle(Long articleId) {

        ArticleEntity articleEntity = articleRepository.findById(articleId)
                .orElseThrow(() -> new BusinessLogicException(ExceptionCode.ARTICLE_NOT_EXIST));

        //신고 + 1
        int complain = articleEntity.getComplain();
        complain++;

        //5회 이상일 경우 게시글 삭제
        if(complain >= 5){
            //쪽지도 삭제할건지?
            //messageRepository.deleteByMessage(messageEntity); //TODO : 게시물 삭제될 경우 쪽지도 동시 삭제 될건지?
            //게시글 삭제
            deleteArticle(articleId);
            return null;
        }
        else {
            articleEntity.setComplain(complain);
            log.info("Complain count: ", complain);
            return articleMapper.toResponseDto(articleEntity);

        }
    }






}
