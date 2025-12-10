package com.example.demo.security;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
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

		log.info("JWT validation per every request");

		String authHeader = request.getHeader("Authorization");
		log.info("Header must have Bearer Authorization with JWT token");

		if (authHeader == null || !authHeader.contains("Bearer ")) {
			log.info("User is not logged in with Bearer Authorization");

			filterChain.doFilter(request, response);
			return;
		}

		String token = authHeader.substring(7);
		log.info("Extract JWT token from every request to validate");

		Claims claims = jwtService.validateTokenAndGetClaims(token);
		log.info("Claims store the user email, role and permissions ...");

		if (claims == null) {
			log.info("Claims is null");

			filterChain.doFilter(request, response);
			return;
		}

		String email = claims.getSubject();

		String role = claims.get("role", String.class);

		List<String> permissions = claims.get("permission", List.class);

		List<GrantedAuthority> authorities = permissions.stream().map(SimpleGrantedAuthority::new)
				.collect(Collectors.toList());
		authorities.add(new SimpleGrantedAuthority("ROLE_" + role));

		log.info("Create Authentication Object to save in security context");

		UsernamePasswordAuthenticationToken authTokenObj = new UsernamePasswordAuthenticationToken(email, null,
				authorities);

		SecurityContextHolder.getContext().setAuthentication(authTokenObj);

		filterChain.doFilter(request, response);
	}

}
