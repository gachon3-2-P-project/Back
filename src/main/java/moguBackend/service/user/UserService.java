package moguBackend.service.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import moguBackend.config.emailConfig.EmailAuthEntity;
import moguBackend.config.emailConfig.EmailRepository;
import moguBackend.config.emailConfig.MailService;
import moguBackend.common.Role;
import moguBackend.domain.dto.ArticleDto;
import moguBackend.domain.entity.ArticleEntity;
import moguBackend.domain.entity.UserEntity;
import moguBackend.domain.dto.UserDto;
import moguBackend.domain.mapper.ArticleMapper;
import moguBackend.exception.BusinessLogicException;
import moguBackend.exception.ExceptionCode;
import moguBackend.domain.mapper.UserMapper;
import moguBackend.repository.user.ArticleRepository;
import moguBackend.repository.user.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final MailService mailService;
    private final EmailRepository emailRepository;
    private final ArticleRepository articleRepository;
    private final ArticleMapper articleMapper;

    @Value("${spring.mail.auth-code-expiration-millis}")
    private long authCodeExpirationMillis;

    /**
     * 인증 코드 보낸 뒤 10 분마다 해당 데이터 삭제
     */

    @Scheduled(fixedRate = 600000)  // 10분마다 실행
    @Transactional
    public void deleteExpiredAuthCodes() {

        // 현재 시간을 기준으로 10분 이전에 만료된 인증 코드를 조회
        long currentTimeMinusOneMinute = System.currentTimeMillis() - 600000;
        log.info(String.valueOf(currentTimeMinusOneMinute));

        // 현재 시간을 기준으로 10분 이전에 만료된 인증 코드를 조회
        List<EmailAuthEntity> expiredAuthCodes = emailRepository.findByAuthCodeExpirationMillisLessThan(currentTimeMinusOneMinute);

        // 만료된 인증 코드 삭제
        for (EmailAuthEntity expiredAuthCode : expiredAuthCodes) {
            emailRepository.delete(expiredAuthCode);
            log.info("Expired Email : {}", expiredAuthCode.getEmail());
        }


    }


    @Transactional
    public void sendCodeToEmail(String toEmail) {
        this.checkDuplicatedEmail(toEmail);
        String title = "MoguMogu 이메일 인증 번호";
        String authCode = this.createCode();
        mailService.sendEmail(toEmail, title, authCode);

        //이메일 인증번호 보낸 후 DB에 저장
        EmailAuthEntity emailAuthEntity = EmailAuthEntity.builder()
                .email(toEmail)
                .authCode(authCode)
                .authCodeExpirationMillis(authCodeExpirationMillis)
                .build();
        emailRepository.save(emailAuthEntity);

    }

    /**
     * 이미 회원가입한 회원인지 확인하는 메서드
     */
    private void checkDuplicatedEmail(String username) {
        Optional<UserEntity> user = Optional.ofNullable(userRepository.findByUsername(username)); //username == emailConfig
        if (user.isPresent()) {
            log.debug("MemberServiceImpl.checkDuplicatedEmail exception occur emailConfig: {}", username);
            throw new BusinessLogicException(ExceptionCode.USER_NOT_FOUND);
        }
    }

    /**
     * 인증 코드 남은 시간 조회
     */


    @Transactional
    public String createCode() {
        int lenth = 6; //6자리 랜덤 코드
        try {
            Random random = SecureRandom.getInstanceStrong();
            StringBuilder builder = new StringBuilder();
            for (int i = 0; i < lenth; i++) {
                builder.append(random.nextInt(10));
            }
            return builder.toString();
        } catch (NoSuchAlgorithmException e) {
            log.debug("MemberService.createCode() exception occur");
            throw new BusinessLogicException(ExceptionCode.NO_SUCH_ALGORITHM);
        }
    }

    @Transactional
    public boolean verifiedCode(String email, String authCode) {
        this.checkDuplicatedEmail(email);

        // 테이블에서 EmailAuthEntity 조회
        EmailAuthEntity emailAuthEntity = emailRepository.findByEmail(email);

        if (emailAuthEntity == null) {
            throw new BusinessLogicException(ExceptionCode.EMAIL_AUTH_NOT_FOUND);
        }

        // 테이블에 저장된 코드와 입력된 코드 비교
        return emailAuthEntity.getAuthCode().equals(authCode);
    }

    /**
     * 회원 생성
     */
    @Transactional
    public UserDto.UserResponseDto createUser(UserDto.UserRequestDto userRequestDto) {


        //encoding
        if (userRequestDto.getPassword() != null)
            userRequestDto.setPassword(bCryptPasswordEncoder.encode(userRequestDto.getPassword()));
        // RequestDto -> Entity
        UserEntity userEntity = userMapper.toRequestEntity(userRequestDto);
        userEntity.setRole(Role.USER);

        // DB에 Entity 저장
        UserEntity savedUser = userRepository.save(userEntity);

        // Entity -> ResponseDto

        return userMapper.toResponseDto(savedUser);
    }

//    /**
//     * 회원 id로 작성한 게시물 조회
//     */
//
//    public UserDto.UserResponseDto getUser(Long userId) {
//        // Entity 조회
//        UserEntity userEntity = userRepository.findById(userId)
//                .orElseThrow(() -> new BusinessLogicException(ExceptionCode.USER_NOT_FOUND));
//
//        // DTO로 변환 후 return
//        return userMapper.toResponseDto(userEntity);
//    }

    /**
     * 회원 id로 작성한 게시물들 조회
     */
    public List<ArticleDto.ArticleResponseDto> getUserArticles(Long userId) {
        // 회원 정보 조회
        UserEntity userEntity = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessLogicException(ExceptionCode.USER_NOT_FOUND));

        // 해당 회원이 작성한 게시물들 조회
        List<ArticleEntity> articles = articleRepository.findByUser(userEntity);

        // DTO로 변환 후 return
        return articles.stream()
                .map(articleMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    /**
     * 전체 회원 조회
     */
    @Transactional
    public List<UserDto.UserResponseDto> getAllUser() {

        List<UserEntity> userEntities = userRepository.getAllUser();
        List<UserDto.UserResponseDto> userResponseDtos = new ArrayList<>();

        for (UserEntity userEntity : userEntities) {
            UserDto.UserResponseDto userResponseDto = userMapper.toResponseDto(userEntity);
            userResponseDtos.add(userResponseDto);
        }

        return userResponseDtos;
    }

    /**
     * 회원정보 수정 -> 닉네임 수정
     */
    @Transactional
    public UserDto.UserResponseDto updateUser(Long userId,UserDto.UserPatchDto userPatchDto) {

        UserEntity userEntity = userRepository.findById(userId)
                .orElseThrow(() ->new BusinessLogicException(ExceptionCode.USER_NOT_FOUND));

        String newNickName = userPatchDto.getNickName();

        // 닉네임 중복성 확인.
        if (newNickName != null) {

            boolean isNickNameUnique = !userRepository.existsByNickName(newNickName);

            if (!isNickNameUnique) {
                throw new BusinessLogicException(ExceptionCode.DUPLICATE_NICKNAME);
            }

            userEntity.setNickName(newNickName);
        }
        return userMapper.toResponseDto(userEntity);
    }


    /**
     * 회원 탈퇴
     */
    @Transactional
    public void deleteUser(Long userId) {
        userRepository.deleteById(userId);
        log.info("삭제된 User 아이디: {}",userId);
    }


}