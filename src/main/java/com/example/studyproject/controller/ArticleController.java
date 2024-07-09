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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
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

        return "redirect:/articles/" + saved.getId();
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

    @GetMapping("/articles")
    public String index(Model model){

        //1. 모든 Article을 가져온다!
        List<Article> articleEntityList = articleRepository.findAll();

        //2. 가져온 Article 묶음을 view로 전달
        model.addAttribute("articleList", articleEntityList);

        //3. 뷰 페이지를 설정!
        return "articles/index";
    }

    @GetMapping("/articles/{id}/edit")
    public String edit(@PathVariable Long id, Model model){
        //수정할 데이터 가져오기
        Article articleEntity = articleRepository.findById(id).orElse(null);

        //모델에 데이터 등록
        model.addAttribute("article", articleEntity);

        //뷰 페이지 설정
        return "articles/edit";
    }

    @PostMapping("/articles/update")
    public String update(ArticleForm form){
        log.info(form.toString());

        //1. DTO를 Entity로 변환
        Article articleEntity = form.toEntity();
        log.info(articleEntity.toString());

        //2. Entity를 DB에 저장
        //2-1. DB에서 기존 데이터를 가져옴
        Article target = articleRepository.findById(articleEntity.getId()).orElse(null);

        //2-2. 기존 데이터의 값을 갱신
        if(target != null){
            articleRepository.save(articleEntity); //Entity가 DB에 갱신
        }

        //3. 수정 결과 페이지로 리다이렉트(재요청)
        return "redirect:/articles/" + articleEntity.getId();
    }

    @GetMapping("/articles/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes rttr){
        log.info("삭제 요청");

        //1. 삭제 대상을 가져오기
        Article target = articleRepository.findById(id).orElse(null);
        log.info(target.toString());

        //2. 그 대상을 삭제
        if(target != null){
            articleRepository.delete(target);
            rttr.addFlashAttribute("msg", "삭제가 완료되었습니다.");
        }

        //3. 결과 페이지로 리다이렉트
        return "redirect:/articles";
    }
}