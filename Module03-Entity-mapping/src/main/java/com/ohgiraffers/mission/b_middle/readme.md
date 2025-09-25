### 🥈 중급 미션: 팀(Team)과 선수(Player)의 관계

**목표:** 일대다(`@OneToMany`), 다대일(`@ManyToOne`) 양방향 연관관계를 설정하고, '연관관계 편의 메서드'의 필요성을 이해하고 구현할 수 있다.

**요구사항:**
1.  `Team`과 `Player` 엔티티를 생성하세요.
2.  `Team`은 `id`, `name` 필드를 가지고, `Player`는 `id`, `name` 필드를 가집니다.
3.  `Team`에서는 소속된 `Player` 목록을, `Player`에서는 자신이 속한 `Team`을 알 수 있도록 양방향 연관관계를 설정하세요.
    * 외래 키는 `Player` 테이블에 생성되어야 합니다 (`Player`가 연관관계의 주인).
4.  `Team` 엔티티에 `addPlayer(Player player)`라는 연관관계 편의 메서드를 작성하여, `Team`에 선수를 추가하면 `Player`의 `team` 정보도 함께 설정되도록 구현하세요.
5.  `Application` 클래스에서 `Team`을 만들고, `addPlayer` 메서드를 사용하여 여러 `Player`를 추가한 뒤, `Team`만 영속화해도 `Player`들이 함께 저장되는지(`CascadeType.PERSIST` 또는 `ALL` 설정 필요) 확인하세요.
