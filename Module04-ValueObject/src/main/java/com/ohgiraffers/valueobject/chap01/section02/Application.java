package com.ohgiraffers.valueobject.chap01.section02;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import java.time.LocalDate;


/*
 * 🏆 2단계: 해답으로서의 값 객체 - "스스로 책임지는 불변 객체"
 *
 * 💡 문제 해결 과정:
 * 1. 개념의 발견: `numberOfGuests` -> `GuestCount` / `checkInDate`, `checkOutDate` -> `StayPeriod`
 * - 흩어져 있던 원시 타입들을 '투숙객 수'와 '숙박 기간'이라는 의미 있는 개념(값 객체)으로 묶었습니다.
 *
 * 2. 책임의 위임:
 * - "투숙객은 1명 이상이어야 한다"는 규칙의 검증 책임을 `GuestCount` 생성자에게 위임했습니다.
 * - "체크아웃은 체크인 이후여야 한다"는 규칙의 검증 책임을 `StayPeriod` 생성자에게 위임했습니다.
 * - `calculateTotalPrice()`는 이제 `stayPeriod.getNights()`에게 숙박일수 계산을 '요청'할 뿐, 직접 계산하지 않습니다. (객체 간의 협력)
 *
 * 3. 불변성의 확보:
 * - `GuestCount`와 `StayPeriod`에는 `setter`가 없습니다. 한번 생성되면 절대 변하지 않는 '불변 객체'입니다.
 * - 이로 인해 `reservation1` 객체가 생성된 이후에 내부의 숙박 기간이나 투숙객 수가 외부에서 임의로 변경될 가능성이 원천 차단됩니다. 시스템의 안정성이 극도로 높아집니다.
 *
 * "JPA의 `@Embedded`는 이처럼 강력한 값 객체를 엔티티의 속성처럼 DB 테이블에 자연스럽게 녹여내는 접착제 역할을 합니다."
 */
public class Application {
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("jpa-lecture");
        EntityManager em = emf.createEntityManager();

        em.getTransaction().begin();

        try {
            StayPeriod period1 = new StayPeriod(
                    LocalDate.of(2025, 8, 1),
                    LocalDate.of(2025, 8, 3)
            );

            GuestCount guests1 = new GuestCount(2);
            Reservation reservation1 = new Reservation("김철수", "101호", guests1, period1, 50000);

            System.out.println("예상 총 가격: " + reservation1.calculateTotalPrice());

            // ❌ 체크인 날짜보다 이전인 체크아웃 날짜를 설정하려는 시도는 예외를 발생시킨다.
            // StayPeriod invalidPeriod = new StayPeriod(LocalDate.of(2025, 9, 5), LocalDate.of(2025, 9, 3));
            // Reservation reservation2 = new Reservation("박영희", "202호", new GuestCount(1), invalidPeriod, 70000);

            // ❌ 투숙객 수를 0 이하로 설정하려는 시도는 예외를 발생시킨다.
            // GuestCount invalidGuests = new GuestCount(0);
            // Reservation reservation3 = new Reservation("최자영", "303호", invalidGuests, period1, 60000);

            em.persist(reservation1);
            em.getTransaction().commit();

        } catch (IllegalArgumentException e) {
            System.err.println("유효하지 않은 값입니다: " + e.getMessage());
            em.getTransaction().rollback();

        } catch (Exception e) {

            e.printStackTrace();
            em.getTransaction().rollback();
        } finally {
            em.close();
        }
        emf.close();
    }
}
