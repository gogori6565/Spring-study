package com.example.studyproject.api;

import com.example.studyproject.dto.ArticleForm;
import com.example.studyproject.entity.Article;
import com.example.studyproject.repository.ArticleRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
public class ArticleApiController {

    @Autowired
    private ArticleRepository articleRepository;

    //GET - 전체 article
    @GetMapping("/api/articles")
    public List<Article> index(){
        return articleRepository.findAll();
    }

    //GET - 단일 article
    @GetMapping("/api/articles/{id}")
    public Article index(@PathVariable Long id){
        return articleRepository.findById(id).orElse(null);
    }

    //POST - 생성
    @PostMapping("/api/articles")
    public Article create(@RequestBody ArticleForm dto){
        Article created_article = dto.toEntity();
        return articleRepository.save(created_article);
    }

    //PATCH
    @PatchMapping("/api/articles/{id}")
    public ResponseEntity<Article> update(@RequestBody ArticleForm dto,
                                         @PathVariable Long id){
        //1. 수정용 엔티티 생성
        Article updated_article = dto.toEntity();
        log.info("id: {}, article: {}", id, updated_article.toString());

        //2. 대상 엔티티 조회
        Article target = articleRepository.findById(id).orElse(null);

        //3. 잘못된 요청 처리 (대상이 없거나, (요청 url과 body의)id가 다른 경우)
        if(target == null || id != updated_article.getId()){
            //400, 잘못된 요청 응답 보내기
            log.info("잘못된 요청! id: {}, article: {}", id, updated_article.toString());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null); //BAD_REQUEST = 400
        }

        //4. 업데이트 및 정상 응답(200)
        target.patch(updated_article);
        Article updated = articleRepository.save(target);
        return ResponseEntity.status(HttpStatus.OK).body(updated); //OK = 200
    }

    //DELETE - 삭제
    @DeleteMapping("/api/articles/{id}")
    public ResponseEntity<Article> delete(@PathVariable Long id){
        //1. 대상 찾기
        Article target = articleRepository.findById(id).orElse(null);

        //2. 잘못된 요청 처리
        if(target == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }

        //3. 대상 삭제
        articleRepository.delete(target);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
