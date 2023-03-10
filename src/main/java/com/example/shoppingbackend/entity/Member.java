package com.example.shoppingbackend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import org.hibernate.annotations.ColumnDefault;

@Getter
@Entity
@Table(name = "members")
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(nullable = false)
    @ColumnDefault("0")
    private int admin; // 일반 회원: 0, 관리자: 1

    @Column(length = 50, nullable = false, unique = true)
    private String email;

    @Column(length = 50 , nullable = false)
    private String password;

}
