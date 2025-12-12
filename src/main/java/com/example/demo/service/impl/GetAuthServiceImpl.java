package com.example.demo.service.impl;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.dto.req.RoleRequestDTO;
import com.example.demo.dto.res.UserInfoResponseDTO;
import com.example.demo.entity.Permission;
import com.example.demo.entity.Role;
import com.example.demo.entity.User;
import com.example.demo.entity.UserRole;
import com.example.demo.enums.RoleEnum;
import com.example.demo.repo.PermissionRepository;
import com.example.demo.repo.RolePermissionRepository;
import com.example.demo.repo.RoleRepository;
import com.example.demo.repo.UserRepository;
import com.example.demo.repo.UserRoleRepository;
import com.example.demo.service.GetAuthService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class GetAuthServiceImpl implements GetAuthService {

	private final RoleRepository roleRepository;
	private final PermissionRepository permissionRepository;
	private final RolePermissionRepository rolePermissionRepository;
	private final UserRepository userRepository;
	private final UserRoleRepository userRoleRepository;

	@Override
	public List<Role> getAllRoles() {
		log.info("Getting all user roles");

		List<Role> roles = roleRepository.findAll();
		return roles;
	}

	@Override
	public List<Permission> getAllPermissions() {
		log.info("Getting all user roles");

		List<Permission> permission = permissionRepository.findAll();

		return permission;
	}

	@Override
	public List<Permission> getAllPermissionsByRoleId(long roleId) {

		log.info("Getting all permission by role id: {} ", roleId);

		List<Permission> permissions = rolePermissionRepository.findPermissionEntitiesByRoleId(roleId);

		return permissions;
	}

	@Override
	@Transactional
	public void assignRoleToUser(RoleRequestDTO request) {
		log.info("Assign Role : {}  to user with id : {} ", request.getRole(), request.getUserId());

		User user = userRepository.findById(request.getUserId())
				.orElseThrow(() -> new RuntimeException("User doesn't exists"));

		userRoleRepository.deleteByUserId(user.getId());

		Role role = roleRepository.findByName(RoleEnum.valueOf(request.getRole()))
				.orElseThrow(() -> new RuntimeException("Role not found"));

		UserRole newUserRole = new UserRole();
		newUserRole.setUser(user);
		newUserRole.setRole(role);

		userRoleRepository.save(newUserRole);

		log.info("Role : {} assigned to user with id : {} successfully", request.getRole(), request.getUserId());
	}

	@Override
	public List<UserInfoResponseDTO> getAllUser() {
		log.info("Getting all users");

		List<User> users = userRepository.findAll();

		List<UserInfoResponseDTO> response = users.stream().map(user -> {
			UserInfoResponseDTO dto = new UserInfoResponseDTO();
			UserRole userRole = userRoleRepository.findByUserId(user.getId())
					.orElseThrow(() -> new RuntimeException("Role doesn't exist for this user : " + user.getEmail()));

			Role role = userRole.getRole();

			dto.setUsername(user.getUsername());
			dto.setEmail(user.getEmail());
			dto.setActive(user.getActive());
			dto.setRole(role.getName().name());
			return dto;
		}).toList();

		log.info("Users found: {} ", users.size());

		return response;
	}

	@Override
	public UserInfoResponseDTO getAllUserByUserId(long userId) {

		User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User doesn't exists"));

		UserRole userRole = userRoleRepository.findByUserId(user.getId())
				.orElseThrow(() -> new RuntimeException("Role doesn't exist for this user "));

		Role role = userRole.getRole();

		List<String> permissions = rolePermissionRepository.findPermissionsByRoleId(role.getId());

		UserInfoResponseDTO dto = new UserInfoResponseDTO();
		dto.setId(user.getId());
		dto.setUsername(user.getUsername());
		dto.setEmail(user.getEmail());
		dto.setActive(user.getActive());
		dto.setPermissions(permissions);
		dto.setRole(role.getName().name());
		dto.setCreatedAt(user.getCreatedAt());
		dto.setUpdatedAt(user.getUpdatedAt());

		return dto;
	}

	@Override
	@Transactional
	public void updateRoleToUser(@Valid RoleRequestDTO request, Long userId) {
		log.info("Update Role : {}  to user with id : {} ", request.getRole(), userId);

		User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User doesn't exists"));

		userRoleRepository.deleteByUserId(user.getId());

		Role role = roleRepository.findByName(RoleEnum.valueOf(request.getRole()))
				.orElseThrow(() -> new RuntimeException("Role not found"));

		UserRole newUserRole = new UserRole();
		newUserRole.setUser(user);
		newUserRole.setRole(role);

		userRoleRepository.save(newUserRole);
		log.info("Role : {} updated to user with id : {} successfully", request.getRole(), request.getUserId());
	}

	@Override
	public void deactivateUser(Long id) {
		log.info("Deactivating user with id: {} ", id);
		User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User doesn't exists"));

		user.setActive(false);
		user.setUpdatedAt(LocalDateTime.now());

		userRepository.save(user);

		log.info("User with id: {} deactivated successfully", id);
	}

	@Override
	public void activateUser(Long id) {

		log.info("Activating user with id: {} ", id);
		User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User doesn't exists"));

		user.setActive(true);
		user.setUpdatedAt(LocalDateTime.now());

		userRepository.save(user);

		log.info("User with id: {} activated successfully", id);

	}

}
