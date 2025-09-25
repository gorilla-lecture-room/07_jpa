package com.ohgiraffers.mission.a_basic;

import jakarta.persistence.*;

@Entity(name="mission_basic_post")
@Table(name = "mission_post")
public class Post {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    protected Post() {}
    public Post(String title) { this.title = title; }
}