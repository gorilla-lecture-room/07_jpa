### 🥉 기초 미션: 댓글(Comment)과 게시글(Post)의 관계

**목표:** 가장 기본이 되는 다대일(`@ManyToOne`) 단방향 연관관계를 이해하고 설정할 수 있다. "하나의 게시글에 여러 댓글이 달린다"는 관계를 객체 모델로 표현합니다.

**요구사항:**
1.  `mission` 패키지 아래에 `Post`와 `Comment` 엔티티를 생성하세요.
2.  `Post` 엔티티는 `id`와 `title` 필드를 가집니다.
3.  `Comment` 엔티티는 `id`와 `content` 필드를 가집니다.
4.  `Comment` 엔티티에서 `Post` 엔티티를 참조하는 `@ManyToOne` 단방향 관계를 설정하세요.
    * `Comment`는 자신이 어떤 `Post`에 속해 있는지 알아야 합니다. (`Comment`가 연관관계의 주인)
5.  `Application` 클래스에서 하나의 `Post`를 생성하고, 여러 개의 `Comment`를 생성하여 해당 `Post`에 속하도록 설정한 후 모두 저장해보세요.
