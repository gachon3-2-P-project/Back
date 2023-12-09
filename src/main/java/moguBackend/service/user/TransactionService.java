package moguBackend.service.user;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import moguBackend.domain.mapper.TransactionMapper;
import moguBackend.repository.user.TransactionRepository;
import moguBackend.repository.user.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class TransactionService {

    private final UserRepository userRepository;
    private final TransactionRepository transactionRepository;
    private final TransactionMapper transactionMapper;

    /**
     * 사용자 거래 승인 요청
     */
//    @Transactional
//    public TransactionDto.TransactionResponseDto createApprove(Long userId, Long articleId) {
//
//        UserEntity userEntity = userRepository.findById(userId)
//                .orElseThrow(() -> new BusinessLogicException(ExceptionCode.USER_NOT_FOUND));
//
//
//    }




}
