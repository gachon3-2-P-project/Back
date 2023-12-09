package moguBackend.controller.user;
import lombok.RequiredArgsConstructor;
import moguBackend.domain.dto.ArticleDto;
import moguBackend.service.user.ArticleService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/article")
public class ArticleController {

    private final ArticleService articleService;

    /**
     * 게시물 등록
     */
    @PostMapping("/create")
    public ResponseEntity<ArticleDto.ArticleResponseDto> createArticle(@RequestParam("userId") Long userId, @RequestBody ArticleDto.ArticleRequestDto articleRequestDto) {
        ArticleDto.ArticleResponseDto responseDto = articleService.createArticle(userId, articleRequestDto);
        return ResponseEntity.ok().body(responseDto);
    }

    /**
     * 게시물 id로 게시물 조회
     */
    @GetMapping("/get")
    public ResponseEntity<?> getArticle(@RequestParam("articleId") Long articleId) {
        ArticleDto.ArticleResponseDto article = articleService.getArticle(articleId);
        return ResponseEntity.ok().body(article);
    }

    /**
     * 게시물 삭제
     */
    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteArticle(@RequestParam Long articleId) {
        articleService.deleteArticle(articleId);
        return ResponseEntity.ok().body("Deleted Article Id : " + articleId);
    }

    /**
     * 게시글 키워드로 검색
     */

    @GetMapping("/search")
    public ResponseEntity<List<ArticleDto.ArticleResponseDto>> searchArticle(@RequestParam String keyword) {
        List<ArticleDto.ArticleResponseDto> articles = articleService.searchArticle(keyword);
        return ResponseEntity.ok().body(articles);

    }

    /**
     * 게시글 신고
     */

    @PatchMapping("/addComplain")
    public ResponseEntity<?> addComplain(@RequestParam("articleId") Long articleId) {
        return ResponseEntity.ok().body(articleService.complainArticle(articleId));
    }

    /**
     * 전체 게시물 조회
     */
    @GetMapping("/getAll")
    public ResponseEntity<List<ArticleDto.ArticleResponseDto>> getAllArticle() {
        List<ArticleDto.ArticleResponseDto> articles = articleService.getAllArticle();
        return ResponseEntity.ok().body(articles);
    }

    /**
     * 해당 게시물 쪽지 조회
     */
    @GetMapping("/getArticleMessages")
    public ResponseEntity<List<ArticleDto.ArticleResponseDto>> getArticleMessages(@RequestParam Long articleId) {
        List<ArticleDto.ArticleResponseDto> articlesWithMessages = articleService.getArticleMessages(articleId);
        return ResponseEntity.ok().body(articlesWithMessages);
    }

    /**
     * 게시물 수정
     */
    @PatchMapping("/update")
    public ResponseEntity<?> updateArticle(@RequestParam("articleId") Long articleId, @RequestBody ArticleDto.ArticlePatchDto articlePatchDto) {
        return ResponseEntity.ok().body(articleService.updateArticle(articleId, articlePatchDto));
    }



}
