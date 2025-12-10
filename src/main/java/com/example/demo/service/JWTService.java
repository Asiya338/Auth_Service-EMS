package com.example.demo.service;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.example.demo.entity.Role;
import com.example.demo.entity.User;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class JWTService {

	@Value("${jwt.secret}")
	private String secret;

	@Value("${jwt.expiration}")
	private long expiration;

	@Value("${jwt.refreshExpiration}")
	private long refreshExpiration;

	private Key getSigningKey() {
		return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
	}

	// ===================== Generate Access Token =====================

	public String generateJwtToken(User user, Role role, List<String> permissions) {

		log.info("Generating JWT access token...");

		return Jwts.builder().setSubject(user.getEmail()).claim("userId", user.getId())
				.claim("role", role.getName().name()).claim("permission", permissions) // FIXED
				.setIssuedAt(new Date()).setExpiration(new Date(System.currentTimeMillis() + expiration))
				.signWith(getSigningKey(), SignatureAlgorithm.HS256) // FIXED
				.compact();
	}

	// ===================== Validate Token =====================

	public Claims validateTokenAndGetClaims(String token) {

		try {
			log.info("Validating JWT token...");

			return Jwts.parserBuilder().setSigningKey(getSigningKey()) // ALWAYS use getSigningKey()
					.build().parseClaimsJws(token).getBody();
		} catch (Exception e) {
			log.error("Invalid or expired JWT: {}", e.getMessage());
			return null;
		}
	}

	// ===================== Refresh Token =====================

	public String generateRefreshToken(User user) {

		log.info("Generating refresh token for user: {}", user.getUsername());

		return Jwts.builder().setSubject(user.getEmail()).setIssuedAt(new Date())
				.setExpiration(new Date(System.currentTimeMillis() + refreshExpiration))
				.signWith(getSigningKey(), SignatureAlgorithm.HS256) // FIXED
				.compact();
	}
}
