package com.example.demo.security;

import java.io.IOException;
import java.util.Collections;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.example.demo.service.JWTService;

import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class JWTAuthenticationFilter extends OncePerRequestFilter {

	private final JWTService jwtService;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		// TODO : extract jwt token and validate

		String authHeader = request.getHeader("Authorization");

		if (authHeader == null || !authHeader.contains("Bearer ")) {
			filterChain.doFilter(request, response);
			return;
		}

		String token = authHeader.substring(7);

		Claims claims = jwtService.validateTokenAndGetClaims(token);

		if (claims == null) {
			filterChain.doFilter(request, response);
			return;
		}

		String email = claims.getSubject();

		UsernamePasswordAuthenticationToken authTokenObj = new UsernamePasswordAuthenticationToken(email, null,
				Collections.emptyList());

		SecurityContextHolder.getContext().setAuthentication(authTokenObj);

		filterChain.doFilter(request, response);
	}

}
