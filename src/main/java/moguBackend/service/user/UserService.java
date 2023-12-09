package moguBackend.service.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import moguBackend.config.email.EmailAuthEntity;
import moguBackend.config.email.EmailRepository;
import moguBackend.config.email.MailService;
import moguBackend.constant.Role;
import moguBackend.domain.user.UserEntity;
import moguBackend.dto.user.UserDto;
import moguBackend.exception.BusinessLogicException;
import moguBackend.exception.ExceptionCode;
import moguBackend.mapper.user.UserMapper;
import moguBackend.repository.user.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private static final String AUTH_CODE_PREFIX = "AuthCode ";
    private final MailService mailService;
    private final EmailRepository emailRepository;

    @Value("${spring.mail.auth-code-expiration-millis}")
    private long authCodeExpirationMillis;


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
        Optional<UserEntity> user = Optional.ofNullable(userRepository.findByUsername(username)); //username == email
        if (user.isPresent()) {
            log.debug("MemberServiceImpl.checkDuplicatedEmail exception occur email: {}", username);
            throw new BusinessLogicException(ExceptionCode.USER_NOT_FOUND);
        }
    }

    /**
     * 인증 코드 남은 시간 조회
     */
    public long getRemainingTimeForAuthCode(String email) {
        EmailAuthEntity emailAuthEntity = emailRepository.findByEmail(email);
        if (emailAuthEntity == null) {
            throw new BusinessLogicException(ExceptionCode.EMAIL_AUTH_NOT_FOUND);
        }

        return emailAuthEntity.getRemainingTimeMillis();
    }

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

    /**
     * 회원 id로 회원 조
     */

    public UserDto.UserResponseDto getUser(Long userId) {
        // Entity 조회
        UserEntity userEntity = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessLogicException(ExceptionCode.USER_NOT_FOUND));

        // DTO로 변환 후 return
        return userMapper.toResponseDto(userEntity);
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