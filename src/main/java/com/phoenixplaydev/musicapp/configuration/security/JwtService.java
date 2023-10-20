package com.phoenixplaydev.musicapp.configuration.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {

    private static final String SECRET_KEY = "JhrACvwjUCIUp24GIZm1oIT387Pdrix8zccUIQvN1kTAfztyH8Zy2ORVSPZ5GwCvE6vhkQoHj+IMsfoTnlmst/0KI/nljm604TdLUfFc+qK++g9uzdykr9BD+cVY8P8MAS6LOP7CcbeStVkEYr+OYFL31dDKmJO+bn2r7GDJVILzjzzCwbUac1TXfNflCJSlnUcxzcfhpAebd2fpFx42dUX3Ba0Sf6Eq7hCzsKY+OF3EW42ncQP+hXVwr1RpDaHiBY0f05lorIsRmqmzU5keupxbGm05/4WbzHkTxBhNXGJViXZ7HWQ4jyeUAAOfKapvPiC7J1lbXSvPnR6oBJEzraQqAwyg0Po8QKaXwhoyaOM=";

    public String extractEmail(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public String generateToken(CredentialsInput credentialsInput) {
        return generateToken(new HashMap<>(), credentialsInput);
    }

    @SuppressWarnings("deprecation")
    public String generateToken(Map<String, Object> extraClaims,
                                CredentialsInput credentials) {
        return Jwts
                .builder()
                .setClaims(extraClaims)
                .setSubject(credentials.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 24))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public boolean isTokenValid(String token, CredentialsInput credentials) {
        final String email = extractEmail(token);
        return (email.equals(credentials.getUsername()) && !isTokenExpired(token));
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    @SuppressWarnings("deprecation")
    public Claims extractAllClaims(String token) {
        return Jwts
                .parser()
                .setSigningKey(getSignInKey())
                .build()
                .parseSignedClaims(token)
                .getBody();
    }

    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }

}
