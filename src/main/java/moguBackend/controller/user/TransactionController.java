package moguBackend.controller.user;

import lombok.RequiredArgsConstructor;
import moguBackend.dto.user.ArticleDto;
import moguBackend.dto.user.TransactionDto;
import moguBackend.service.user.ArticleService;
import moguBackend.service.user.TransactionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("transaction")
public class TransactionController {

    private TransactionService transactionService;


    /**
     * 전체 게시물 조회
     */

    /**
     * 관리자 전체 거래 조회
     */



    /**
     * 사용자 거래 승인 요청
     */

    /**
     * 사용자 거래 완료 요청
     */

    /**
     * 관리자 사용자 거래 승인 확인
     */

    /**
     * 관리자 사용자 거래 완료 확인
     */

}
