package com.ohgiraffers.chap02.model;

import jakarta.persistence.*;

import java.time.LocalDate;



/*
 * 객체지향(OOP)과 RDB의 패러다임 차이: DTO 사용 문제와 JPA의 해결
 *
 * 📌 JPA가 이 문제를 해결하는 방법:
 * JPA(Java Persistence API)는 객체지향과 RDB 간의 패러다임 차이를 해결하기 위해 매핑 메커니즘을 제공한다.
 * - `@Entity`와 `@Table(name = "users")`를 사용해 `User` 클래스를 RDB의 `Users` 테이블과 매핑한다.
 * - `@ManyToOne`과 `@JoinColumn(name = "role_id")`를 사용해 `User`와 `Role` 간의 관계를 객체지향적으로 정의한다.
 *   이를 통해 `User`가 `Role` 객체를 직접 참조하도록 설계할 수 있으며, RDB의 외래 키(`role_id`)를 기반으로 관계를 매핑한다.
 * - DTO에서처럼 평면화된 데이터(`roleName` 필드)를 직접 포함시키는 대신, `Role` 객체를 참조(`private Role role`)함으로써 객체지향의 캡슐화와 관계를 유지한다.
 * - JPA는 내부적으로 조인 쿼리를 자동으로 생성하여 `Role` 데이터를 조회하므로, 개발자가 직접 조인 쿼리를 작성할 필요가 없다.
 *   예: `user.getRole().getRoleName()`을 호출하면 JPA가 `Roles` 테이블에서 해당 데이터를 조회한다.
 * - 이를 통해 객체지향의 설계 원칙(캡슐화, 객체 간 협력)을 유지하면서 RDB의 데이터를 효과적으로 다룰 수 있다.
 * - 또한, JPA는 엔티티 객체에 동작(메서드)을 추가할 수 있으므로, DTO와 달리 객체지향의 풍부한 표현력을 유지할 수 있다.
 *
 * 📌 해결 결과:
 * JPA를 사용하면 `User`와 `Role`을 별도의 엔티티로 관리하여 객체지향적인 설계를 유지할 수 있다.
 * - `User`와 `Role` 간의 관계를 평면화된 DTO로 표현하지 않고, 객체 참조(`Role role`)로 관리함으로써 캡슐화와 객체 간 협력을 보장.
 * - `user.getRole()`과 같은 방식으로 관계를 탐색할 수 있으며, JPA가 내부적으로 필요한 조인 쿼리를 처리.
 * - DTO 사용으로 인한 객체지향 설계의 손실(캡슐화 약화, 동작 제한)을 방지하고, 객체지향과 RDB 간의 패러다임 차이를 조정
 */

/*
* 아래의 내용은 이후에 자세하게 다룬다.
* 현재는 JPA가 RDB와 패러다임 불일치를 어떤식으로 해결하고 있는지에 집중한다.
* */
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int userId;
    @Column(nullable = false, name = "user_id")
    private String username;
    @Column(nullable = false, name = "email")
    private String email;
    @Column(nullable = false, name = "password_hash")
    private String passwordHash;

    @ManyToOne
    @JoinColumn(name = "role_id") // 외래 키 컬럼 지정
    private Role role;
    @Column(nullable = false, name = "createdAt")
    private LocalDate createdAt;

    public User() {
    }

    public User(int userId, String username, String email, String passwordHash, Role role, LocalDate createdAt) {
        this.userId = userId;
        this.username = username;
        this.email = email;
        this.passwordHash = passwordHash;
        this.role = role;
        this.createdAt = createdAt;
    }

    /*
     * 살아있는 객체 - 스스로 일하게 하라
     *
     * 💡 엔티티는 데이터 저장소가 아니다. '행위(Behavior)'를 가져야 한다.
     * - 기존처럼 getter/setter만 가진 객체를 '빈혈적인 도메인 모델(Anemic Domain Model)'이라고 부른다.
     * - 이런 모델은 객체의 데이터를 외부 서비스 계층에서 마음대로 꺼내어 조작하므로, 캡슐화가 깨지고 객체지향의 장점을 잃게 된다.
     *
     * 💡 어떻게 개선할까요?
     * - 객체가 스스로의 데이터를 책임지고 상태를 변경하는 메서드를 제공해야 한다.
     * - 예를 들어, 이메일을 변경하는 로직은 User 객체 스스로가 가장 잘 안다.
     *
     * - (Before) 서비스 로직: `user.setEmail("new@email.com");` -> User는 수동적, 서비스가 모든 일을 함.
     * - (After) 객체지향 로직: `user.changeEmail("new@email.com");` -> User에게 '이메일 변경'을 요청, User가 능동적으로 자신의 상태를 변경.
     *
     * 이 작은 차이가 유지보수성과 코드의 명확성을 극적으로 향상시킵니다.
     */
    public void changeEmail(String newEmail) {
        // 이메일 형식 검증과 같은 비즈니스 로직이 이곳에 포함될 수 있습니다.
        if (newEmail == null || !newEmail.contains("@")) {
            throw new IllegalArgumentException("유효하지 않은 이메일 형식입니다.");
        }
        this.email = newEmail;
    }


    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
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

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public LocalDate getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDate createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "User{" +
                "userId=" + userId +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", passwordHash='" + passwordHash + '\'' +
                ", role=" + role +
                ", createdAt=" + createdAt +
                '}';
    }
}
