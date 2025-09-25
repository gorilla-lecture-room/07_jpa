package com.ohgiraffers.z_activity.mission.c_deep;

import com.ohgiraffers.z_activity.mission.b_middle.Address;
import jakarta.persistence.*;

@Entity
@Table(name = "orders") // order는 SQL 예약어이므로 orders 사용 권장
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "zipcode", column = @Column(name = "shipping_zipcode")),
            @AttributeOverride(name = "address1", column = @Column(name = "shipping_addr1")),
            @AttributeOverride(name = "address2", column = @Column(name = "shipping_addr2"))
    })
    private Address shippingAddress;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "zipcode", column = @Column(name = "billing_zipcode")),
            @AttributeOverride(name = "address1", column = @Column(name = "billing_addr1")),
            @AttributeOverride(name = "address2", column = @Column(name = "billing_addr2"))
    })
    private Address billingAddress;

    protected Order() {}

    public Order(Address shippingAddress, Address billingAddress) {
        this.shippingAddress = shippingAddress;
        this.billingAddress = billingAddress;
    }
}
