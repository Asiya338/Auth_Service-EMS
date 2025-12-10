package com.example.demo.service;

import java.util.Date;
import java.util.List;

import javax.crypto.SecretKey;

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

	private SecretKey secretKey;

	@Value("${jwt.expiration}")
	private long expiration;

	@Value("${jwt.secret}")
	private String secret;

	@Value("${jwt.refreshExpiration}")
	private long refreshExpiration;

	public String generateJwtToken(User user, Role role, List<String> permissions) {
		secretKey = Keys.hmacShaKeyFor(secret.getBytes());

		log.info("Generating JWT token based on email, role and permissions...");

		return Jwts.builder().setSubject(user.getEmail()).claim("userId", user.getId())
				.claim("role", role.getName().name()).claim("permission", permissions).setIssuedAt(new Date())
				.setExpiration(new Date(System.currentTimeMillis() + expiration))
				.signWith(secretKey, SignatureAlgorithm.HS256).compact();
	}

	public Claims validateTokenAndGetClaims(String token) {
		try {
			log.info("Validating and returning jwt claims to authenticate based role and permissions");

			return Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token).getBody();
		}

		catch (Exception e) {

			log.error("Invalid or expired JWT: {} " + e.getMessage());

			return null;
		}
	}

	public String generateRefreshToken(User user) {
		log.info("Generating refreh token for user : {} ", user.getUsername());

		return Jwts.builder().setSubject(user.getEmail()).setIssuedAt(new Date())
				.setExpiration(new Date(System.currentTimeMillis() + refreshExpiration)) // 7 days
				.signWith(secretKey, SignatureAlgorithm.HS256).compact();

	}

}
