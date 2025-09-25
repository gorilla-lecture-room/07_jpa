package com.ohgiraffers.z_activity.mission.b_middle;

import com.ohgiraffers.chap01.section02.Role;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(nullable = false, unique = true, length = 100)
    private String username;

    @Column(nullable = false, length = 100)
    private String email;
    @Column(name = "password_hash", nullable = false, length = 255)
    private String password;

    @Column(name = "birth_date")
    private LocalDate birthDate; // 생년월일

    @Column(name = "created_at")
    private LocalDateTime createdAt; // 계정 생성일시

    @Enumerated(EnumType.STRING)
    @Column(name = "role_id", nullable = false)
    private Role role;

    @Embedded
    private Address address;


    protected User() {}
}
