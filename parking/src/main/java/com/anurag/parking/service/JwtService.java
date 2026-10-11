package com.anurag.parking.service;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;
import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
@Service
public class JwtService {
    private final SecretKey signingKey;
    public JwtService(@Value("${parking.jwt.secret}") String secret) {
        byte[] bytes = secret.getBytes(StandardCharsets.UTF_8);
        if (bytes.length < 32) throw new IllegalStateException("PARKING_JWT_SECRET must be at least 32 UTF-8 bytes");
        this.signingKey = Keys.hmacShaKeyFor(bytes);
    }
    private SecretKey key(){return signingKey;}
    public String generateToken(String email){
        return Jwts.builder().subject(email).issuedAt(new Date())
            .expiration(new Date(System.currentTimeMillis()+86400000)).signWith(key()).compact();
    }
    public String extractEmail(String token){return Jwts.parser().verifyWith(key()).build().parseSignedClaims(token).getPayload().getSubject();}
    public boolean validateToken(String token){try{Jwts.parser().verifyWith(key()).build().parseSignedClaims(token);return true;}catch(Exception e){return false;}}
}
