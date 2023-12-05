package moguBackend.service.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import moguBackend.domain.user.UserEntity;
import moguBackend.dto.user.UserDto;
import moguBackend.exception.BusinessLogicException;
import moguBackend.exception.ExceptionCode;
import moguBackend.mapper.user.UserMapper;
import moguBackend.repository.user.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

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