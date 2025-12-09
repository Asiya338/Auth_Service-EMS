package com.example.demo.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.Permission;
import com.example.demo.enums.PermissionEnum;

public interface PermissionRepository extends JpaRepository<Permission, Long> {

	Optional<Permission> findByName(PermissionEnum pEnum);
}
