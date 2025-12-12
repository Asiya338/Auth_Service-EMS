package com.example.demo.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.UserRole;

public interface UserRoleRepository extends JpaRepository<UserRole, Long> {

	Optional<UserRole> findByUserId(Long id);

	void deleteByUserId(Long id);

}
