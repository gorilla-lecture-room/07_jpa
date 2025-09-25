package com.ohgiraffers.inheritance.mission.b_middle;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.Lob;

@Entity
@DiscriminatorValue("POST")
public class Post extends Content {
    @Lob
    private String body;
    protected Post() {}
    public Post(String title, String body) {
        super(title);
        this.body = body;
    }
}