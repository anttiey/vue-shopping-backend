package com.example.shoppingbackend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column
    private int memberId;
    @Column(length = 50, nullable = false) /* 이름 */
    private String name;
    @Column(length = 100, nullable = false) /* 주소 */
    private String address;
    @Column(length = 10, nullable = false) /* 결제 수단 */
    private String payment;
    @Column(length = 16) /* 카드 번호 */
    private String cardNumber;
    @Column(length = 100, nullable = false) /* 구입한 아이템 */
    private String items;

}
