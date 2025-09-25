package com.ohgiraffers.valueobject.mission.b_middle;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "mission_order")
public class Order {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String orderName;

    @ElementCollection
    @CollectionTable(name = "order_options", joinColumns = @JoinColumn(name = "order_id"))
    private List<OrderOption> options = new ArrayList<>();

    protected Order() {}

    public Order(String orderName) {
        this.orderName = orderName;
    }

    // 행위(Behavior) 추가: 옵션을 추가하는 책임
    public void addOption(OrderOption option) {
        this.options.add(option);
    }

    // 행위(Behavior) 추가: 총 옵션 가격을 계산하는 책임
    public int getTotalOptionPrice() {
        return this.options.stream()
                .mapToInt(OrderOption::getPrice)
                .sum();
    }
}