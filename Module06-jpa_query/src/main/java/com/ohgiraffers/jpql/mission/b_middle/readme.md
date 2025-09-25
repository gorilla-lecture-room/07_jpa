
### 🥈 중급 미션: 특정 강좌의 강의(Lesson) 목록과 평균 가격 조회

**목표:** `JOIN`을 사용하여 연관된 객체를 탐색하고, 집계 함수(`AVG`)와 `GROUP BY`를 객체지향적으로 사용하는 방법을 익힙니다.

**요구사항:**
1.  모든 강좌(`Course`)에 대해, 각 강좌의 제목(`title`)과 해당 강좌에 속한 강의(`Lesson`)의 개수, 그리고 해당 강좌의 가격을 조회하는 JPQL을 작성하세요.
2.  `JOIN`을 사용하여 `Course`와 `Lesson`을 연결해야 합니다.
3.  `GROUP BY`를 사용하여 강좌별로 그룹화하세요.
4.  조회 결과를 담을 `CourseInfoDTO` 클래스를 직접 만들어, `new` 키워드를 사용한 프로젝션으로 결과를 받으세요.
    * `CourseInfoDTO`는 `title(String)`, `lessonCount(Long)`, `price(double)` 필드를 가져야 합니다.
5.  `Application`에서 조회된 DTO 목록을 출력하세요.
