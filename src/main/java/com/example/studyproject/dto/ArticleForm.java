package com.example.studyproject.dto;

import com.example.studyproject.entity.Article;

public class ArticleForm {

    private String title;
    private String content;

    //우클릭 > Generate > Constructor : 생성자
    public ArticleForm(String title, String content) {
        this.title = title;
        this.content = content;
    }

    //우클릭 > Generate > toString() : 출력해서 보기
    @Override
    public String toString() {
        return "ArticleForm{" +
                "title='" + title + '\'' +
                ", content='" + content + '\'' +
                '}';
    }

    public Article toEntity() {
        return new Article(null, title, content);
    }
}
