package com.ohgiraffers.z_activity.mission.a_basic;


import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "post")
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String title;

    @Lob // Large Object, CLOB 또는 BLOB 타입으로 매핑
    @Column(nullable = false)
    private String content;

    private String author;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private PostStatus status;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    protected Post() {}

    public Post(String title, String content, String author, PostStatus status) {
        this.title = title;
        this.content = content;
        this.author = author;
        this.status = status;
        this.createdAt = LocalDateTime.now();
    }
}
