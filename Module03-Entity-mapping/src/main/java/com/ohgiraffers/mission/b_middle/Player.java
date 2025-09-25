package com.ohgiraffers.mission.b_middle;

import jakarta.persistence.*;

@Entity(name="mission_intermediate_player")
@Table(name = "mission_player")
public class Player {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

    @ManyToOne
    @JoinColumn(name = "team_id")
    private Team team;

    protected Player() {}
    public Player(String name) { this.name = name; }

    // 연관관계 편의 메서드용 setter
    public void setTeam(Team team) { this.team = team; }
}