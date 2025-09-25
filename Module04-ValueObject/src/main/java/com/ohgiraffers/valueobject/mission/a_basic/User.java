package com.ohgiraffers.valueobject.mission.a_basic;

import jakarta.persistence.*;

@Entity
@Table(name = "mission_user")
public class User {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private FullName fullName;

    protected User() {}

    public User(FullName fullName) {
        this.fullName = fullName;
    }
}