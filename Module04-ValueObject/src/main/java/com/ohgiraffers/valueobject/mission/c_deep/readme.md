### 🥇 고급 미션: 재료(`Ingredient`) 값 객체 컬렉션 만들기

**목표:** `@Embeddable`로 만든 값 객체의 컬렉션을 `@ElementCollection`으로 매핑하는, 실무에서 매우 유용한 패턴을 익힙니다.

**요구사항:**
1. `Ingredient`(재료)라는 이름의 값 객체를 만드세요. (`@Embeddable` 사용)
2. `Ingredient`는 다음 필드를 가집니다.
    * `name`: `String` 타입 (재료명)
    * `quantity`: `double` 타입 (양)
    * `unit`: `String` 타입 (단위, 예: "g", "개", "ml")
3. `Recipe`(레시피)라는 엔티티를 만들고, `List<Ingredient> ingredients` 필드를 포함시키세요.
4. `@ElementCollection`과 `@CollectionTable`을 사용하여 `ingredients` 필드를 `recipe_ingredients` 테이블에 매핑하세요.
5. `Application`에서 `Recipe` 객체에 여러 `Ingredient`를 추가하여 저장하고, `recipe_ingredients` 테이블에 `name`, `quantity`, `unit` 컬럼들이 잘 생성되고 데이터가 저장되는지 확인하세요.
