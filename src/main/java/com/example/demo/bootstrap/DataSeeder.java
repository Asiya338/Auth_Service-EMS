package com.example.demo.bootstrap;

import java.util.List;
import java.util.Optional;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.example.demo.constants.Constant;
import com.example.demo.entity.Permission;
import com.example.demo.entity.Role;
import com.example.demo.entity.RolePermission;
import com.example.demo.entity.User;
import com.example.demo.entity.UserRole;
import com.example.demo.enums.PermissionEnum;
import com.example.demo.enums.RoleEnum;
import com.example.demo.repo.PermissionRepository;
import com.example.demo.repo.RolePermissionRepository;
import com.example.demo.repo.RoleRepository;
import com.example.demo.repo.UserRepository;
import com.example.demo.repo.UserRoleRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@RequiredArgsConstructor
public class DataSeeder implements CommandLineRunner {

	private final UserRepository userRepository;
	private final RoleRepository roleRepository;
	private final PermissionRepository permissionRepository;
	private final PasswordEncoder passwordEncoder;
	private final RolePermissionRepository rolePermissionRepository;
	private final UserRoleRepository userRoleRepository;

	@Override
	public void run(String... args) throws Exception {
		seedRoles();
		seedPermissions();
		seedRolePermissions();
		seedAdminUser();
	}

	private void seedRoles() {
		for (RoleEnum roleEnum : RoleEnum.values()) {
			Optional<Role> existing = roleRepository.findByName(roleEnum);

			if (existing.isEmpty()) {
				Role role = new Role();
				role.setName(roleEnum);

				log.info("Role : {} ", role);

				roleRepository.save(role);
			}
		}
	}

	private void seedPermissions() {
		for (PermissionEnum pEnum : PermissionEnum.values()) {
			Optional<Permission> existing = permissionRepository.findByName(pEnum);

			if (existing.isEmpty()) {
				Permission permission = new Permission();
				permission.setName(pEnum);

				log.info("Permission : {} ", permission);

				permissionRepository.save(permission);
			}
		}
	}

	private void seedRolePermissions() {
		Role adminRole = roleRepository.findByName(RoleEnum.ADMIN).orElse(null);

		Role employeeRole = roleRepository.findByName(RoleEnum.EMPLOYEE).orElse(null);

		// admin -> all permissions

		List<Permission> allPerms = permissionRepository.findAll();
		log.info("All permissions to ADMIN ");

		for (Permission permission : allPerms) {
			if (!existsRolePermission(adminRole, permission)) {
				saveRolePermission(adminRole, permission);
			}
		}

		// employee -> limited permissions

		List<PermissionEnum> limitedPerms = List.of(PermissionEnum.ATTENDANCE_MARK, PermissionEnum.ATTENDANCE_VIEW,
				PermissionEnum.PAYROLL_VIEW, PermissionEnum.LEAVE_APPLY, PermissionEnum.EMPLOYEE_READ);
		log.info("Limited permissions to EMPLOYEE");

		for (PermissionEnum pEnum : limitedPerms) {
			Permission perm = permissionRepository.findByName(pEnum).orElseThrow();

			if (!existsRolePermission(employeeRole, perm)) {
				saveRolePermission(employeeRole, perm);
			}
		}
	}

	private void seedAdminUser() {
		String email = Constant.EMAIL;

		if (userRepository.existsByEmail(email)) {
			throw new RuntimeException("Email already exists");
		}

		User user = new User();
		user.setUsername(Constant.USERNAME);
		user.setEmail(email);
		user.setPassword(passwordEncoder.encode(Constant.PASSWORD));
		user.setActive(true);

		userRepository.save(user);

		Role adminRole = roleRepository.findByName(RoleEnum.ADMIN).orElseThrow();

		UserRole userRole = new UserRole();
		userRole.setUser(user);
		userRole.setRole(adminRole);

		userRoleRepository.save(userRole);

	}

	private void saveRolePermission(Role role, Permission permission) {

		RolePermission rolePerm = new RolePermission();
		rolePerm.setRole(role);
		rolePerm.setPermission(permission);

		rolePermissionRepository.save(rolePerm);
	}

	private boolean existsRolePermission(Role role, Permission permission) {

		return rolePermissionRepository.existsByRoleIdAndPermissionId(role.getId(), permission.getId());
	}
}