package com.ohgiraffers.chap01.section02;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

/*
 * 학습 목표: 단순한 데이터 타입을 '의미 있는' 비즈니스 속성으로 진화시키기
 *
 * 💡 문제 상황:
 * 만약 회원의 역할(role)을 `@Column private String role;` 처럼 단순한 문자열로 관리한다면 어떤 문제가 생길까요?
 * - 오타 발생: "ADMIN", "admin", "administrator" 등 다양한 값이 저장되어 데이터 일관성이 깨집니다.
 * - 확장성 부족: 역할에 따른 권한 레벨 같은 추가 정보를 담기 어렵습니다.
 *
 * 🤔 해결 아이디어:
 * "개발자의 실수를 방지하고, '역할'이라는 개념을 타입 수준에서 안전하게 다룰 수는 없을까?"
 * -> 이것이 바로 Enum 타입을 사용하는 이유입니다.
 *
 * 💡 이 섹션의 핵심 질문:
 * - `@Column`의 `nullable`, `unique` 같은 제약조건은 왜 객체(Entity) 단에서 설정하는 것이 좋을까요?
 * - `@Enumerated(EnumType.STRING)`은 왜 `ORDINAL`보다 압도적으로 권장될까요?
 * - `LocalDate`, `LocalDateTime`을 사용하면 왜 날짜 관련 비즈니스 로직이 더 명확해질까요?
 */
@Entity
@Table(name = "users")
public class User {
    /*
     * 📌 @Id + @GeneratedValue
     *
     * - @Id는 엔티티의 기본 키(PK)를 의미합니다.
     * - @GeneratedValue는 자동 생성 전략을 지정합니다.
     *   (IDENTITY는 MySQL 등에서 사용하는 auto_increment 방식)
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    /*
     * 📌 @Column의 속성들
     *
     * ▶ nullable = false → NOT NULL 제약조건 설정
     * ▶ unique = true → 유일성 보장 (UNIQUE 제약조건 생성)
     * ▶ length = 100 → VARCHAR(100)
     *
     * 💡 문자 타입(String)에만 length 속성이 적용됨
     */
    @Column(nullable = false, unique = true, length = 100)
    private String username;

    @Column(nullable = false, length = 100)
    private String email;

    /*
     * 📌 비밀번호는 일반 텍스트가 아니라 해시된 형태로 저장되어야 합니다.
     * ▶ 보안상 길이가 길 수 있으므로 VARCHAR(255)로 설정
     */
    @Column(name = "password_hash", nullable = false, length = 255)
    private String password;

    /*
     * 📌 날짜 및 시간 타입 매핑
     *
     * ▶ LocalDate: 날짜만 저장 (yyyy-MM-dd)
     * ▶ LocalDateTime: 날짜 + 시간 저장 (yyyy-MM-dd HH:mm:ss)
     *
     * 🎯 JPA는 Hibernate 5 이상에서 java.time 패키지를 자동 지원함
     */
    @Column(name = "birth_date")
    private LocalDate birthDate; // 생년월일

    @Column(name = "created_at")
    private LocalDateTime createdAt; // 계정 생성일시


    /*
     *  @Enumerated - Enum 타입 필드 매핑
     * EnumType.STRING을 써야만 하는 이유
     * - `ORDINAL` (기본값): Enum의 순서(0, 1, 2...)를 DB에 저장한다.
     * - 치명적 단점: 나중에 Enum 순서가 바뀌면 (예: `ADMIN`과 `INSTRUCTOR` 순서를 바꾸면) 기존의 모든 데이터가 전부 깨진다!
     *   (0번이 학생이었는데 강사가 됨)
     * - `STRING`: Enum의 이름("STUDENT", "INSTRUCTOR"...)을 DB에 저장한다.
     * - 강력한 장점: 순서가 바뀌거나 중간에 새로운 역할이 추가되어도 기존 데이터에 전혀 영향을 주지 않는다.
     *   데이터베이스만 봐도 무슨 역할인지 명확하게 알 수 있다.
     *
     * "코드는 언젠가 변합니다. 변화에 안전한 코드가 좋은 코드입니다.
     * 따라서 실무에서는 반드시 `EnumType.STRING`을 사용해야 한다."
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "role_id", nullable = false)
    private Role role;

    // ✅ 기본 생성자 (JPA가 내부적으로 리플렉션으로 객체를 생성할 수 있게 해줍니다)
    protected User() {}

    public User(String username, String email, String password, LocalDate birthDate, Role role) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.birthDate = birthDate;
        this.createdAt = LocalDateTime.now();
        this.role = role;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", birthDate=" + birthDate +
                ", createdAt=" + createdAt +
                ", role=" + role +
                '}';
    }
}
