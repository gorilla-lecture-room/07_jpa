package com.ohgiraffers.mission.b_middle;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity(name="mission_intermediate_team")
@Table(name = "mission_team")
public class Team {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

    @OneToMany(mappedBy = "team", cascade = CascadeType.PERSIST) // 저장 시 Player도 함께 저장
    private List<Player> players = new ArrayList<>();

    protected Team() {}
    public Team(String name) { this.name = name; }

    // 연관관계 편의 메서드
    public void addPlayer(Player player) {
        this.players.add(player);
        player.setTeam(this);
    }
}