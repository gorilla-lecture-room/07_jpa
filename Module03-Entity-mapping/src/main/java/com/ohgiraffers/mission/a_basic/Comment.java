package com.ohgiraffers.mission.a_basic;

import jakarta.persistence.*;

@Entity(name="mission_basic_comment")
@Table(name = "mission_comment")
public class Comment {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String content;

    @ManyToOne
    @JoinColumn(name = "post_id") // 외래 키 컬럼명 지정
    private Post post;

    protected Comment() {}
    public Comment(String content, Post post) {
        this.content = content;
        this.post = post;
    }
}