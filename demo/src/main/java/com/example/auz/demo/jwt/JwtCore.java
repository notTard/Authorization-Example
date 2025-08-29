package com.example.auz.demo.jwt;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import com.example.auz.demo.UserDetails.UserDetailsImpl;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtCore {
    
    @Value("${auz.jwt.secret}")
    public String secret;

    @Value("${auz.jwt.expirationMs}")
    public long lifetime;

    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    public String generateToken(Authentication authentication){
        UserDetailsImpl  userDetails = (UserDetailsImpl)authentication.getPrincipal();

        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        return Jwts.builder()
                .subject(userDetails.getUsername())
                .claim("roles",roles)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis()+lifetime))
                .signWith(getSigningKey(),Jwts.SIG.HS256)
                .compact();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token);
            
            return true;
        } catch (ExpiredJwtException expEx) {
            // Токен просрочен
            System.err.println("Token expired: " + expEx.getMessage());
        } catch (UnsupportedJwtException unsEx) {
            // Неподдерживаемый токен
            System.err.println("Unsupported jwt: " + unsEx.getMessage());
        } catch (MalformedJwtException mjEx) {
            // Неправильный формат токена
            System.err.println("Malformed jwt: " + mjEx.getMessage());
        } catch (SignatureException sEx) {
            // Неверная подпись
            System.err.println("Invalid signature: " + sEx.getMessage());
        } catch (Exception e) {
            // Остальные исключения
            System.err.println("Invalid token: " + e.getMessage());
        }
        return false;
    }

    public String getNameFromJwt(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }
}
