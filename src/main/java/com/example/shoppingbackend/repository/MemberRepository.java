package com.example.shoppingbackend.repository;

import com.example.shoppingbackend.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Integer> {
    Member findByEmailAndPassword(String email, String password);
    Member findById(int id);
}