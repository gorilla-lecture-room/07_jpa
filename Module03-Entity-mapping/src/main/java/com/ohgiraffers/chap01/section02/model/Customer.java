package com.ohgiraffers.chap01.section02.model;


import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity(name = "section02-customer")
@Table(name = "customers")
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "customer_id")
    private Long id;

    @Column(name = "name")
    private String name;


    /*
     * 📌 양방향 연관관계 매핑
     * - 고객은 여러 주문을 가질 수 있다 (1:N)
     * - mappedBy: 연관관계의 주인이 되는 엔티티에서 정의한 필드의 이름을 명시한다.
     *             즉, Order의 customer(fk)를 지정함.
     * = 연관관계 주인은 외래 키(FK)를 가지고 있으며, 해당 관계의 상태를 직접적으로 관리하는 것을 의미한다.
     *
     * 📌 Cascade 옵션
     * - cascade = CascadeType.ALL:
     *   고객 저장 시 관련된 "모든 주문도 함께 저장"된다.
     *   예를 들어, 고객을 저장하면 그 고객의 모든 주문도 자동으로 저장된다.
     *   다른 CascadeType 옵션:
     *   - PERSIST: 고객 저장 시 주문도 저장
     *   - REMOVE: 고객 삭제 시 주문도 삭제
     *   - MERGE: 고객 병합 시 주문도 병합
     *   - REFRESH: 고객 새로 고침 시 주문도 새로 고침
     *   - NONE : 아무런 전파가 없는 것을 의미하며 (기본 값)
     *
     * 📌 Orphan Removal 옵션
     * - orphanRemoval = true:
     *   고객에서 특정 주문을 제거할 경우, 해당 주문이 DB에서도 삭제된다.
     *   즉, 고객과의 연관이 끊어진 주문은 데이터베이스에서 자동으로 삭제된다.
     *
     * 🎯 주의: 컬렉션은 초기화를 반드시 해줘야 함 (new ArrayList<>())
     */
    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL)
    private List<Order> orders = new ArrayList<>();

    protected Customer() {}

    public Customer(String name) {
        this.name = name;
    }


    /*
     * 🏆 연관관계 편의 메서드: 객체 세상의 일관성을 지키는 책임
     *
     * 💡 왜 이 메서드가 중요할까요?
     * - `orders.add(order);` 만 호출하면 `Customer`는 `Order`를 알지만, `Order`는 아직 자신의 `customer`가 누구인지 모르는 '짝사랑' 상태가 됩니다.
     * - 반대로 `order.setCustomer(this);`만 호출하면 `Order`는 `Customer`를 알지만, `Customer`의 주문 목록에는 `Order`가 빠져있습니다.
     *
     * 💡 객체 그래프의 일관성
     * - JPA를 떠나 순수 객체 상태에서 양쪽의 관계가 모두 설정되어야 데이터의 신뢰성이 보장됩니다.
     * - 이처럼 양쪽의 관계를 한 번에 설정해주는 메서드를 '연관관계 편의 메서드'라고 부릅니다.
     * - 이 메서드를 통해 `addOrder`의 책임은 온전히 `Customer`가 갖게 되며, 외부에서는 이 메서드를 호출하는 것만으로 안전하게 관계를 설정할 수 있습니다.
     *
     * "연관관계의 주인(Order)이 아닌 쪽(Customer)에서 이 메서드를 제공하여 관계 설정의 책임을 지는 것이 일반적인 설계 패턴입니다."
     */
    public void addOrder(Order order) {
        orders.add(order);
        order.setCustomer(this); // 양방향 연결
    }

    public List<Order> getOrders() {
        return orders;
    }

    public String getName() {
        return name;
    }

    public Long getId() {
        return id;
    }
}