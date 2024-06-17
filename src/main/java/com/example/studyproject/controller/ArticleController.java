package com.example.studyproject.controller;

import com.example.studyproject.dto.ArticleForm;
import com.example.studyproject.entity.Article;
import com.example.studyproject.repository.ArticleRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Optional;

@Controller
@Slf4j //로깅을 위한 어노테이션
public class ArticleController {

    @Autowired //(DI-의존성 주입)스프링 부트가 미리 생성해놓은 객체를 가져다가 자동으로 연결!
    private ArticleRepository articleRepository;

    //폼 작성 view
    @GetMapping("/articles/new")
    public String newArticleForm(){
        return "articles/new";
    }

    //폼 데이터 DTO로 받고 -> Entity 변환 -> Repository 이용해 DB 저장
    @PostMapping("/articles/create")
    public String createArticle(ArticleForm form) {
        //System.out.println(form.toString()); -> 로깅으로 대체!
        log.info(form.toString());

        //1. Dto를 Entity로 변환!
        Article article = form.toEntity();
        log.info(article.toString());

        //2. Repository에게 Entity를 DB안에 저장하게 함!
        Article saved = articleRepository.save(article); //.save() - CrudRepository에 정의되어 있는 기능 사용
        log.info(saved.toString());

        return "";
    }

    @GetMapping("/articles/{id}")
    public String show(@PathVariable Long id, Model model){
        log.info("id: "+id);

        //1. id로 데이터를 가져옴
        Article articleEntity = articleRepository.findById(id).orElse(null);

        //2. 가져온 데이터를 모델에 등록
        model.addAttribute("article", articleEntity);

        //3. 보여줄 페이지를 설정!
        return "articles/show";
    }
}