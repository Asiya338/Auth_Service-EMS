package com.example.demo.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.demo.dto.LoginRequestDTO;
import com.example.demo.dto.LoginResponseDTO;
import com.example.demo.dto.RegisterRequestDTO;
import com.example.demo.dto.RegisterResponseDTO;
import com.example.demo.dto.SetPasswordRequestDTO;
import com.example.demo.entity.PasswordResetToken;
import com.example.demo.entity.Permission;
import com.example.demo.entity.Role;
import com.example.demo.entity.User;
import com.example.demo.entity.UserRole;
import com.example.demo.enums.PasswordStatusEnum;
import com.example.demo.enums.RoleEnum;
import com.example.demo.repo.PasswordResetTokenRepository;
import com.example.demo.repo.PermissionRepository;
import com.example.demo.repo.RolePermissionRepository;
import com.example.demo.repo.RoleRepository;
import com.example.demo.repo.UserRepository;
import com.example.demo.repo.UserRoleRepository;
import com.example.demo.service.AuthService;
import com.example.demo.service.JWTService;

import io.jsonwebtoken.Claims;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

	private final UserRepository userRepository;
	private final RoleRepository roleRepository;
	private final PermissionRepository permissionRepository;
	private final PasswordEncoder passwordEncoder;
	private final RolePermissionRepository rolePermissionRepository;
	private final UserRoleRepository userRoleRepository;
	private final JWTService jwtService;
	private final PasswordResetTokenRepository passwordResetTokenRepository;

	@Override
	public LoginResponseDTO login(LoginRequestDTO request) {

		User user = userRepository.findByEmail(request.getEmail())
				.orElseThrow(() -> new RuntimeException("Invalid email or password"));

		if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
			log.error("Password mismatch. Try with correct password ");

			throw new RuntimeException("Password mismatch ");
		}

		UserRole userRole = userRoleRepository.findByUserId(user.getId())
				.orElseThrow(() -> new RuntimeException("No role for this user"));

		Role role = userRole.getRole();

		List<String> permissions = rolePermissionRepository.findPermissionsByRoleId(role.getId());

		String jwtToken = jwtService.generateJwtToken(user, role, permissions);

//		String refreshToken = jwtService.generateRefreshToken(user);

		return new LoginResponseDTO(jwtToken, "Bearer", user.getEmail(), role.getName().name(), null, null,
				permissions);
	}

	@Override
	public LoginResponseDTO refreshToken(String refreshToken) {
		log.info("Get claims by validating refresh token ");

		Claims claims = jwtService.validateTokenAndGetClaims(refreshToken);

		if (claims == null) {
			throw new RuntimeException("Invalid or expired access token");
		}

		String email = claims.getSubject();

		User user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User doesn't exists"));

		UserRole userRole = userRoleRepository.findByUserId(user.getId())
				.orElseThrow(() -> new RuntimeException("User`s Role doesn't exists"));

		Role role = userRole.getRole();

		List<String> permissions = rolePermissionRepository.findPermissionsByRoleId(role.getId());

		String accessToken = jwtService.generateJwtToken(user, role, permissions);
		String refreshToken2 = jwtService.generateRefreshToken(user);

		return new LoginResponseDTO(accessToken, "Bearer", email, role.getName().name(), refreshToken2, null,
				permissions);
	}

	@Override
	public RegisterResponseDTO registerUser(RegisterRequestDTO request) {
		log.info("Register User with user details");

		if (userRepository.findByEmail(request.getEmail()).isPresent()) {
			throw new RuntimeException("User already exists");
		}

		User user = new User();
		user.setUsername(request.getUsername());
		user.setEmail(request.getEmail());
		user.setPassword(passwordEncoder.encode("TEMP_DUMMY_PASSWORD_1234"));

		User savedUser = userRepository.save(user);

		Role role;
		if (request.getRole() != null) {
			role = roleRepository.findByName(RoleEnum.valueOf(request.getRole()))
					.orElseThrow(() -> new RuntimeException("Invalid role"));
		} else {
			role = roleRepository.findByName(RoleEnum.EMPLOYEE).orElseThrow(() -> new RuntimeException("Invalid role"));
		}

		UserRole userRole = new UserRole();
		userRole.setUser(savedUser);
		userRole.setRole(role);

		userRoleRepository.save(userRole);

		List<Permission> permissions = rolePermissionRepository.findPermissionEntitiesByRoleId(role.getId());

		PasswordResetToken resetToken = new PasswordResetToken();
		resetToken.setToken(UUID.randomUUID().toString());
		resetToken.setUser(savedUser);
		resetToken.setExpiryAt(LocalDateTime.now().plusHours(24)); // valid for 24 hours

		passwordResetTokenRepository.save(resetToken);

		String setPasswordUrl = "http://localhost:8086/auth/set-password?token=" + resetToken.getToken();

		return new RegisterResponseDTO(savedUser.getEmail(), role.getName().toString(), setPasswordUrl);

	}

	@Override
	public void setUserPassword(@Valid SetPasswordRequestDTO request) {

		log.info("Set Password Request");

		PasswordResetToken passwordResetToken = passwordResetTokenRepository.findByToken(request.getToken());

		if (passwordResetToken.getExpiryAt().isBefore(LocalDateTime.now())) {
			log.error("Token expired. Unable to set password");

			throw new RuntimeException("Token expired");
		}

		User user = passwordResetToken.getUser();

		user.setPassword(passwordEncoder.encode(request.getNewPassword()));
		user.setPasswordStatus(PasswordStatusEnum.ACTIVE);
		user.setUpdatedAt(LocalDateTime.now());

		userRepository.save(user);

		passwordResetTokenRepository.delete(passwordResetToken);

		log.info("Password set successfully for user : {} ", user.getEmail());
	}

}
