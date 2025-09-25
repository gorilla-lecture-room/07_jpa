package com.ohgiraffers.valueobject.chap01.section01;

import java.time.LocalDate;

/*
 * 🏆 1단계: 문제 직면하기 - "원시 타입에 대한 집착(Primitive Obsession)"
 *
 * 💡 문제 상황:
 * 현재 `Reservation` 클래스는 `guestName(String)`, `numberOfGuests(int)`, `checkInDate(LocalDate)` 등 모든 데이터를
 * 원시 타입(Primitive Type)이나 JDK 기본 제공 객체로 다루고 있습니다. 이를 '원시 타입 집착'이라고 부르며, 다음과 같은 문제를 야기합니다.
 *
 * 1. 데이터의 의미가 없다: `int numberOfGuests`는 그냥 '숫자'일 뿐, '투숙객 수'라는 비즈니스 의미나 제약조건(예: 0명 이하는 안됨)을 담지 못합니다.
 * 2. 유효성 검증의 부재: `checkInDate`가 `checkOutDate`보다 늦을 수 있는 등, 데이터가 논리적으로 말이 안 되는 상태로 생성될 수 있습니다. `calculateNights()`가 음수를 반환하는 것이 그 증거입니다.
 * 3. 응집력 부족: 숙박 기간(체크인/체크아웃)과 관련된 계산 로직(`calculateNights`)이 `Reservation`에 흩어져 있습니다. 숙박 기간 계산이 필요한 다른 곳이 있다면 코드가 중복될 것입니다.
 *
 * 🤔 해결 아이디어:
 * "관련 있는 데이터와 그 데이터를 다루는 로직을 하나의 덩어리(객체)로 묶어서, 그 자체로 의미를 가지는 '개념'으로 만들 수 없을까?"
 * -> 이것이 바로 '값 객체(Value Object)'의 탄생 배경입니다.
 */

public class Reservation {

    private String guestName;
    private String roomNumber;
    private int numberOfGuests;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
    private int totalPrice;

    public Reservation(String guestName, String roomNumber, int numberOfGuests, LocalDate checkInDate, LocalDate checkOutDate, int totalPrice) {
        this.guestName = guestName;
        this.roomNumber = roomNumber;
        this.numberOfGuests = numberOfGuests;
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
        this.totalPrice = totalPrice;
    }

    public int calculateNights() {
        // 😥 단순히 두 날짜의 차이를 계산할 뿐,
        // 체크인 날짜가 체크아웃 날짜보다 늦는 경우에 대한 검증이 없다.
        // 만약 이쪽에 체크인 체크아웃을 검증하는 프로세스를 추가하여도
        // 해당 체크인 체크아웃의 데이터를 필요로하는 모든 객체에서도 해당 코드를 작성해야 한다.
        // EX) 고객 관리 시스템에서는 고객의 예약을 확인할 때 체크인 및 체크아웃 날짜의 유효성을 검증해야 한다.
        return (int) checkOutDate.toEpochDay() - (int) checkInDate.toEpochDay();
    }

    public String getGuestName() {
        return guestName;
    }

    public String getRoomNumber() {
        return roomNumber;
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }

    public LocalDate getCheckInDate() {
        return checkInDate;
    }

    public LocalDate getCheckOutDate() {
        return checkOutDate;
    }

    public int getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(int totalPrice) {
        // 😥 총 가격에 대한 어떠한 제약 조건도 없으며
        // 이는 음수 값이나 0이 될 수도 있다.
        this.totalPrice = totalPrice;
    }
}