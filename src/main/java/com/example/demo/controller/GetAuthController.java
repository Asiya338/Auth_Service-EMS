package com.example.demo.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.req.RoleRequestDTO;
import com.example.demo.dto.res.UserInfoResponseDTO;
import com.example.demo.entity.Permission;
import com.example.demo.entity.Role;
import com.example.demo.service.GetAuthService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/v1/auth")
public class GetAuthController {

	private final GetAuthService authService;

	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping("/roles")
	public ResponseEntity<List<Role>> getAllRoles() {
		log.info("get all user roles");

		List<Role> roles = authService.getAllRoles();

		log.info("Roles : {} ", roles);

		return ResponseEntity.ok(roles);
	}

	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping("/permissions")
	public ResponseEntity<List<Permission>> getAllPermissions() {
		log.info("get all user permissions");

		List<Permission> permissions = authService.getAllPermissions();

		log.info("permissions : {} ", permissions);

		return ResponseEntity.ok(permissions);
	}

	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping("/roles/{roleId}/permissions")
	public ResponseEntity<List<Permission>> getAllPermissionsByRoleId(@PathVariable long roleId) {
		log.info("get all user permissions by role id : {} ", roleId);

		List<Permission> permissions = authService.getAllPermissionsByRoleId(roleId);

		log.info("permissions by role id : {} ||  : {} ", roleId, permissions);

		return ResponseEntity.ok(permissions);
	}

	@PreAuthorize("hasRole('ADMIN')")
	@PostMapping("/assign-role")
	public ResponseEntity<String> assignUserRole(@Valid @RequestBody RoleRequestDTO request) {
		log.info("Assign role to user : {} ", request.getUserId());

		authService.assignRoleToUser(request);

		log.info("Role assigned to user successfully : {} , {} ", request.getRole(), request.getUserId());

		return ResponseEntity.ok("Role assigned to user successfully");
	}

	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping("/users")
	public ResponseEntity<List<UserInfoResponseDTO>> getAllUser() {
		log.info("Get all users");

		List<UserInfoResponseDTO> users = authService.getAllUser();

		log.info("All users : {} ", users);

		return ResponseEntity.ok(users);
	}

	@PreAuthorize("hasRole('ADMIN')")
	@PostMapping("/users/{userId}/role")
	public ResponseEntity<String> updateUserRole(@PathVariable long userId,
			@Valid @RequestBody RoleRequestDTO request) {
		log.info("Updtae role to user : {} ", request.getUserId());

		authService.updateRoleToUser(request);

		log.info("Role updated to user successfully : {} , {} ", request.getRole(), request.getUserId());

		return ResponseEntity.ok("Role updated to user successfully");
	}

	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping("/users/{userId}")
	public ResponseEntity<UserInfoResponseDTO> getAllUserByUserId(@PathVariable long userId) {
		log.info("Get user by ID : {} ", userId);

		UserInfoResponseDTO user = authService.getAllUserByUserId(userId);

		log.info("User with ID : {} ", user);

		return ResponseEntity.ok(user);
	}

	@PreAuthorize("hasRole('ADMIN')")
	@PostMapping("/users/{id}/deactivate")
	public ResponseEntity<String> deactivateUser(@PathVariable Long id) {
		log.info("Deactivating user with id: {} ", id);

		authService.deactivateUser(id);

		log.info("User with id: {} deactivated successfully", id);

		return ResponseEntity.ok("User deactivated successfully");
	}

	@PreAuthorize("hasRole('ADMIN')")
	@PostMapping("/users/{id}/activate")
	public ResponseEntity<String> activateUser(@PathVariable Long id) {
		log.info("Activating user with id: {} ", id);

		authService.activateUser(id);

		log.info("User with id: {} activated successfully", id);

		return ResponseEntity.ok("User activated successfully");
	}

}
