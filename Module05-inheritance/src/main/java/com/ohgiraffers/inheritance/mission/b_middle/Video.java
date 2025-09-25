package com.ohgiraffers.inheritance.mission.b_middle;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("VIDEO")
public class Video extends Content {
    private String url;
    protected Video() {}
    public Video(String title, String url) {
        super(title);
        this.url = url;
    }
}