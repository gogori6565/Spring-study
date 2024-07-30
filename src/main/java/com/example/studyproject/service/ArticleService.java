package com.example.studyproject.service;

import com.example.studyproject.dto.ArticleForm;
import com.example.studyproject.entity.Article;
import com.example.studyproject.repository.ArticleRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service //서비스 선언! (서비스 객체를 스프링 객체에 생성)
public class ArticleService {

    @Autowired
    private ArticleRepository articleRepository;

    public List<Article> index() {
        return articleRepository.findAll();
    }

    public Article show(Long id) {
        return articleRepository.findById(id).orElse(null);
    }

    public Article create(ArticleForm dto) {
        Article article = dto.toEntity();

        //POST 요청으로 기존 articlel이 수정되는 것 방지
        if(article.getId() != null){
            return null;
        }
        return articleRepository.save(article);
    }

    public Article update(ArticleForm dto, Long id) {
        Article article = dto.toEntity();
        log.info("id: {}, article: {}", id, article.toString());
        Article target = articleRepository.findById(id).orElse(null);

        if(target == null || id != article.getId()){
            log.info("잘못된 요청! id: {}, article: {}", id, article.toString());
            return null;
        }

        target.patch(article);
        return articleRepository.save(target);
    }

    public Article delete(Long id) {
        Article target = articleRepository.findById(id).orElse(null);

        if(target == null){
            return null;
        }

        articleRepository.delete(target);
        return target;
    }

    @Transactional //해당 메소드를 트랜잭션으로 묶는다!
    public List<Article> createArticles(List<ArticleForm> dtos) {
        //1. dto 묶음을 entity 묶음으로 변환
        List<Article> articleList = dtos.stream()
                .map(dto -> dto.toEntity())
                .collect(Collectors.toList());

        //2. entity 묶음을 DB로 저장
        articleList.stream()
                .forEach(article -> articleRepository.save(article));

        //3. 강제 예외 발생 (롤백 보려고)
        articleRepository.findById(-1L).orElseThrow(
                () -> new IllegalArgumentException("결제 실패!")
        );

        //4. 결과값 반환
        return articleList;
    }
}
