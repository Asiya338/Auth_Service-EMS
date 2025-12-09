package com.example.demo.service.impl;

import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.demo.dto.LoginRequestDTO;
import com.example.demo.dto.LoginResponseDTO;
import com.example.demo.entity.Role;
import com.example.demo.entity.User;
import com.example.demo.entity.UserRole;
import com.example.demo.repo.PermissionRepository;
import com.example.demo.repo.RolePermissionRepository;
import com.example.demo.repo.RoleRepository;
import com.example.demo.repo.UserRepository;
import com.example.demo.repo.UserRoleRepository;
import com.example.demo.service.AuthService;
import com.example.demo.service.JWTService;

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

		return new LoginResponseDTO(jwtToken, "Bearer", user.getEmail(), role.getName().name(), null, null,
				permissions);
	}

}
