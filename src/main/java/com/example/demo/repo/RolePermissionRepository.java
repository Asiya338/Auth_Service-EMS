package com.example.demo.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.demo.entity.RolePermission;

public interface RolePermissionRepository extends JpaRepository<RolePermission, Long> {

	boolean existsByRoleIdAndPermissionId(Long id, Long id2);

	@Query("SELECT p.name FROM RolePermission rp JOIN rp.permission p WHERE rp.role.id = :roleId")
	List<String> findPermissionsByRoleId(Long roleId);

}
