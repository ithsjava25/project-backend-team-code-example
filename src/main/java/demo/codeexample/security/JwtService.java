package demo.codeexample.security;

import demo.codeexample.user.domain.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;

/* Creates and validates Tokens*/

@Service
public class JwtService {

    /*Why @Value("${jwt.secret}")? Injects the value from application.properties at runtime.
    The class doesn't hardcode secrets — it reads them from configuration.*/

    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.expiration}")
    private long expirationMs;

    //Builds the signing key from our secret string
    private SecretKey getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public Long extractUserId(String token) {
        return parseClaims(token).get("userId", Long.class);
    }

    // Called after successful login — creates the token
    public String generateToken(User user) {
        return Jwts.builder()
                .subject(user.getEmail())           // who this token belongs to
                .claim("role", user.getRole())      // extra data inside payload
                .issuedAt(new Date())               // when it was created
                .expiration(new Date(System.currentTimeMillis() + expirationMs))
                .signWith(getSigningKey())          // sign with our secret
                .compact();                         // build the final string
    }

    // Extracts email from token — used to find user on each request
    public String extractEmail(String token) {
        return parseClaims(token).getSubject();
    }

    // Extracts role from token
    /*Why store role inside the token?
    So on every request, you can immediately know the user's role without hitting the database.
    The token is self-contained.*/

    public String extractRole(String token) {
        return parseClaims(token).get("role", String.class);
    }

    // Is this token still valid?
    public boolean isTokenValid(String token) {
        try {
            parseClaims(token); // throws if expired or tampered
            return true;
        } catch (JwtException e) {
            return false; // expired, malformed, wrong signature etc.
        }
    }

    // Does the actual cryptographic verification
    private Claims parseClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey()) // checks signature
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

}
