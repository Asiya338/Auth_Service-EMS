package com.example.demo.service;

import java.util.List;

import com.example.demo.dto.req.RoleRequestDTO;
import com.example.demo.dto.res.UserInfoResponseDTO;
import com.example.demo.entity.Permission;
import com.example.demo.entity.Role;

import jakarta.validation.Valid;

public interface GetAuthService {

	List<Role> getAllRoles();

	List<Permission> getAllPermissions();

	List<Permission> getAllPermissionsByRoleId(long roleId);

	void assignRoleToUser(RoleRequestDTO request);

	List<UserInfoResponseDTO> getAllUser();

	UserInfoResponseDTO getAllUserByUserId(long userId);

	void updateRoleToUser(@Valid RoleRequestDTO request);

	void deactivateUser(Long id);

	void activateUser(Long id);

}
