package com.example.demo.controller;

import java.security.Principal;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.req.ChangePasswordRequestDTO;
import com.example.demo.dto.req.LoginRequestDTO;
import com.example.demo.dto.req.RefreshTokenRequestDTO;
import com.example.demo.dto.req.RegisterRequestDTO;
import com.example.demo.dto.req.SetPasswordRequestDTO;
import com.example.demo.dto.req.ValidateTokenRequestDTO;
import com.example.demo.dto.res.LoginResponseDTO;
import com.example.demo.dto.res.RegisterResponseDTO;
import com.example.demo.dto.res.UserInfoResponseDTO;
import com.example.demo.dto.res.ValidateTokenResponseDTO;
import com.example.demo.service.AuthService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/v1/auth")
public class AuthController {

	private final AuthService authService;

	@PostMapping("/login")
	public ResponseEntity<LoginResponseDTO> login(@Valid @RequestBody LoginRequestDTO request) {
		log.info("Auth Login");

		LoginResponseDTO response = authService.login(request);

		log.info("Login successfully");

		return ResponseEntity.ok(response);
	}

	@PostMapping("/refresh-token")
	public ResponseEntity<LoginResponseDTO> refreshToken(@Valid @RequestBody RefreshTokenRequestDTO request) {
		log.info("Generate Refresh token");

		LoginResponseDTO response = authService.refreshToken(request.getRefreshToken());

		log.info("Generate Refresh token successfully");

		return ResponseEntity.ok(response);
	}

	@PostMapping("/register")
	public ResponseEntity<RegisterResponseDTO> registerUser(@Valid @RequestBody RegisterRequestDTO request) {
		log.info("Registering user ");

		RegisterResponseDTO response = authService.registerUser(request);

		log.info("User registered with email and password");

		return ResponseEntity.ok(response);
	}

	@PostMapping("/set-password")
	public ResponseEntity<String> setUserPassword(@Valid @RequestBody SetPasswordRequestDTO request) {

		log.info("Set user password ");

		authService.setUserPassword(request);

		log.info("User password is set successfully");

		return ResponseEntity.ok("Password is set successfully. You can log in");
	}

	@PreAuthorize("isAuthenticated()")
	@PostMapping("/change-password")
	public ResponseEntity<String> changeUserPassword(@Valid @RequestBody ChangePasswordRequestDTO request,
			Principal principal) {
		log.info("Change authenticated user password ");

		authService.changeUserPassword(request, principal.getName());

		log.info("User password is changed successfully");

		return ResponseEntity.ok("Password changed successfully. You can log in with new Password");
	}

	@PreAuthorize("isAuthenticated()")
	@GetMapping("/me")
	public ResponseEntity<UserInfoResponseDTO> getMyDetails(Principal principal) {
		log.info("Get logged in user details");

		UserInfoResponseDTO response = authService.getLoggedInUser(principal.getName());

		log.info("Fetched User details successfully");

		return ResponseEntity.ok(response);
	}

	@PostMapping("/validate-token")
	public ResponseEntity<ValidateTokenResponseDTO> validateToken(@Valid @RequestBody ValidateTokenRequestDTO request) {
		log.info("Validate Token for external microservices");

		ValidateTokenResponseDTO response = authService.validateToken(request.getToken());

		log.info("Token Validated successfully");

		return ResponseEntity.ok(response);
	}

}
