package com.ohgiraffers.mission.c_deep;

import jakarta.persistence.*;

@Entity(name="mission_advanced_profile")
@Table(name = "mission_profile")
public class Profile {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nickname;

    @OneToOne
    @JoinColumn(name = "member_id")
    private Member member;

    protected Profile() {}
    public Profile(String nickname) { this.nickname = nickname; }

    public void setMember(Member member) { this.member = member; }
}