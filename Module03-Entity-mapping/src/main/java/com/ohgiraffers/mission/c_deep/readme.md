### 🥇 고급 미션: 회원(Member)과 프로필(Profile)의 관계

**목표:** 일대일(`@OneToOne`) 관계를 이해하고, 생명주기가 완전히 동일한 관계에서 `Cascade`와 `orphanRemoval` 옵션의 역할을 이해한다. "회원이 존재해야만 프로필이 존재한다"는 강한 결합 관계를 설계합니다.

**요구사항:**
1.  `Member`와 `Profile` 엔티티를 생성하세요.
2.  `Member`는 `id`, `username` 필드를, `Profile`은 `id`, `nickname` 필드를 가집니다.
3.  `Member`와 `Profile` 간의 일대일 양방향 관계를 설정하세요.
    * 외래 키는 `Profile` 테이블에 두는 것이 일반적입니다.
4.  `Member`를 저장하면 `Profile`도 함께 저장되고, `Member`에서 `Profile`과의 연관관계를 끊으면(`member.setProfile(null)`) `Profile` 데이터가 DB에서 삭제되도록 `CascadeType.ALL`과 `orphanRemoval = true` 옵션을 설정하세요.
    * `orphanRemoval`은 '고아 객체 제거' 기능으로, 부모와의 연관관계가 끊어진 자식 객체를 DB에서 자동으로 삭제합니다. 일대일, 일대다 관계에서만 사용할 수 있는 강력한 기능입니다.
5.  `Application`에서 회원을 생성하고 프로필을 설정하여 저장한 뒤, 다시 회원을 찾아 프로필을 `null`로 변경하고 트랜잭션을 커밋했을 때 `Profile` 테이블에서 해당 데이터가 삭제되는 것을 확인하세요.
