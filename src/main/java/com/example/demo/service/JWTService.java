package com.example.demo.service;

import java.util.Date;
import java.util.List;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.example.demo.entity.Role;
import com.example.demo.entity.User;

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

	public String generateJwtToken(User user, Role role, List<String> permissions) {
		secretKey = Keys.hmacShaKeyFor(secret.getBytes());

		return Jwts.builder().setSubject(user.getEmail()).claim("userId", user.getId())
				.claim("role", role.getName().name()).claim("permission", permissions).setIssuedAt(new Date())
				.setExpiration(new Date(System.currentTimeMillis() + expiration))
				.signWith(secretKey, SignatureAlgorithm.HS256).compact();
	}

}
