package com.api.poling.auth.jwt;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import com.api.poling.auth.security.CustomUserDetails;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtTokenService {

    public static byte[] key = "inanilmaz-super-uber-gizli-keyim-tam-burada".getBytes();

    public String generateToken(Authentication auth) {

        CustomUserDetails user = (CustomUserDetails) auth.getPrincipal();
        return Jwts
                .builder()
                .setSubject(user.getUsername())
                .claim("authorities", auth.getAuthorities())
                .setIssuedAt(new Date())
                .setExpiration(java.sql.Date.valueOf(LocalDate.now().plusWeeks(2)))
                .signWith(Keys.hmacShaKeyFor(key))
                .compact();
    }

    public String getUsernameFromToken(String header) {
        return Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(key))
                .build()
                .parseClaimsJws(header)
                .getBody()
                .getSubject();
    }

    @SuppressWarnings("unchecked")
    public List<Map<String, String>> getRoleFromToken(String header) {
        return (List<Map<String, String>>) Jwts
                .parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(key))
                .build()
                .parseClaimsJws(header)
                .getBody()
                .get("authorities");
    }

}
