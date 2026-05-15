package learn.scoreshelf.domain;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import learn.scoreshelf.models.AppUser;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.sql.Date;
import java.time.Instant;

@Service
public class JWTService {

    private final String secret = "super-secret-string-that-is-very-long";

    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    public String generateToken(AppUser user){
        Instant now = Instant.now();

        return Jwts.builder()
                .subject(user.getUsername())
                .claim("appUserId", user.getAppUserId())
                .claim("email", user.getEmail())
                .issuedAt(Date.from(now))
                .expiration(Date.from(now.plusSeconds(60*60*24)))
                .signWith(getSigningKey())
                .compact();
    }

    public String getUsernameFromToken(String token){
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }

    public boolean isTokenValid(String token){
        try{
            Jwts.parser()
                    .verifyWith(getSigningKey())
                    .build()
                    .parseSignedClaims(token);
            return true;
        } catch (Exception ex){
            return false;
        }
    }

}
