package com.ohgiraffers.mission.c_deep;


import jakarta.persistence.*;

@Entity(name="mission_advanced_member")
@Table(name = "mission_member")
public class Member {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;

    @OneToOne(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private Profile profile;

    protected Member() {}
    public Member(String username) { this.username = username; }

    public void setProfile(Profile profile) {
        this.profile = profile;
        if (profile != null) {
            profile.setMember(this);
        }
    }
}