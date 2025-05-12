package my.cousework.barista_helper.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;

import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import my.cousework.barista_helper.api.dto.JwtResponse;
import my.cousework.barista_helper.api.exceptions.AccessDeniedException;
import my.cousework.barista_helper.api.services.UserService;
import my.cousework.barista_helper.store.entities.UserEntity;
import my.cousework.barista_helper.store.enums.Role;

import org.springframework.beans.factory.annotation.Value;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.crypto.SecretKey;

import lombok.AccessLevel;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Service
@RequiredArgsConstructor
public class JwtTokenProvider {
    @Value("${jwt.secret}")
    String jwtSecret;
    @Value("${jwt.access}")
    int jwtExpirationInMs;
    @Value("${jwt.refresh}")
    long jwtRefreshExpirationInMs;
    final UserDetailsService userDetailsService;
    final UserService userService;
    SecretKey key;

    @PostConstruct
    public void init() {
        this.key = Keys.hmacShaKeyFor(jwtSecret.getBytes());
    }

    public String generateAccessToken(final Long userId, final String userEmail, final Set<Role> roles) {
        Claims claims = Jwts.claims()
            .subject(userEmail)
            .add("id", userId)
            .add("roles", resolveRoles(roles))
            .build();

        Instant validity = Instant.now().plus(jwtExpirationInMs, ChronoUnit.HOURS);

        return Jwts.builder()
            .claims(claims)
            .expiration(Date.from(validity))
            .signWith(key)
            .compact();
    }

    private List<String> resolveRoles(final Set<Role> roles) {
        return roles.stream()
            .map(Enum::name)
            .collect(Collectors.toList());
    }

    public String generateRefreshToken(final Long userId, final String userEmail) {
        Claims claims = Jwts.claims()
            .subject(userEmail)
            .add("id", userId)
            .build();
        
        Instant validity = Instant.now().plus(jwtRefreshExpirationInMs, ChronoUnit.DAYS);
        return Jwts.builder()
            .claims(claims)
            .expiration(Date.from(validity))
            .signWith(key)
            .compact();
    }

    public JwtResponse refreshUserTokens(final String refreshToken) {
        JwtResponse jwtResponse = new JwtResponse();
        if (!isValid(refreshToken)) {
            throw new AccessDeniedException("Access denied");
        }
        Long userId = Long.valueOf(getId(refreshToken));
        UserEntity user = userService.getById(userId);
        jwtResponse.setId(userId);
        jwtResponse.setLogin(user.getEmail());
        jwtResponse.setAccessToken(
            generateAccessToken(userId, user.getEmail(), user.getRoles())
        );
        jwtResponse.setRefreshToken(
            generateRefreshToken(userId, user.getEmail())
        );
        return jwtResponse;
    }

    public boolean isValid(final String token) {
        Jws<Claims> claims = Jwts
                .parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token);
        return claims.getPayload()
                .getExpiration()
                .after(new Date());
    }

    private String getId(final String token) {
        return Jwts
                .parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .get("id", String.class);
    }

    private String getUsername(final String token) {
        return Jwts
                .parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }

    public Authentication getAuthentication(final String token) {
        String username = getUsername(token);
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        return new UsernamePasswordAuthenticationToken(
                userDetails,
                "",
                userDetails.getAuthorities()
        );
    }

}
