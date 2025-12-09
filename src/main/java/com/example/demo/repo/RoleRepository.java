package com.example.demo.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.Role;
import com.example.demo.enums.RoleEnum;

public interface RoleRepository extends JpaRepository<Role, Long> {

	Optional<Role> findByName(RoleEnum name);

	Optional<Role> findByUserId(Long id);
}
