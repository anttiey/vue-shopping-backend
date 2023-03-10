package com.example.shoppingbackend.service;

import io.jsonwebtoken.Claims;

public interface JwtService {
    String getToken(Object id, Object admin);
    Claims getClaims(String token);
    boolean isValid(String token);
    int getId(String token);
    int getAdmin(String token);

}

