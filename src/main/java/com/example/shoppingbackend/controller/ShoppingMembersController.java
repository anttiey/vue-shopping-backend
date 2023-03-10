package com.example.shoppingbackend.controller;

import com.example.shoppingbackend.entity.Member;
import com.example.shoppingbackend.repository.MemberRepository;
import com.example.shoppingbackend.service.JwtService;
import com.example.shoppingbackend.service.JwtServiceImpl;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class ShoppingMembersController {
    @Autowired
    MemberRepository memberRepository;

    @Autowired
    JwtService jwtService;

    @PostMapping("/api/account/login")
    public ResponseEntity login(@RequestBody Map<String, String> params, HttpServletResponse res) {
        System.out.println("Email: " + params.get("email") + " Password: " + params.get("password"));
        Member member = memberRepository.findByEmailAndPassword(params.get("email"), params.get("password"));

        if(member == null) {
           throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        int id = member.getId();
        int admin = member.getAdmin();
        String token = jwtService.getToken(id, admin); /* 토큰 생성 */

        Cookie cookie = new Cookie("token", token);
        cookie.setHttpOnly(true);
        cookie.setPath("/");

        res.addCookie(cookie);

        return new ResponseEntity<>(id, HttpStatus.OK);
    }

    @PostMapping("/api/account/logout")
    public ResponseEntity logout(HttpServletResponse res) {
        Cookie cookie = new Cookie("token", null);

        cookie.setPath("/");
        cookie.setMaxAge(0);
        res.addCookie(cookie);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/api/account/check")
    public ResponseEntity check(@CookieValue(value = "token", required = false) String token) {
        Claims claims = jwtService.getClaims(token);

        if (claims != null) {
            int id = jwtService.getId(token);
            int admin = jwtService.getAdmin(token);

            Map<String, Object> result = new HashMap<>();

            result.put("id", id);
            result.put("admin", admin);

            return new ResponseEntity<>(result, HttpStatus.OK);
        }

        return new ResponseEntity<>(null, HttpStatus.OK);
    }

}
