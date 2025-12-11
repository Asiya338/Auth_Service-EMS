package com.example.demo.service;

import com.example.demo.dto.ChangePasswordRequestDTO;
import com.example.demo.dto.LoginRequestDTO;
import com.example.demo.dto.LoginResponseDTO;
import com.example.demo.dto.RegisterRequestDTO;
import com.example.demo.dto.RegisterResponseDTO;
import com.example.demo.dto.SetPasswordRequestDTO;
import com.example.demo.dto.UserInfoResponseDTO;

import jakarta.validation.Valid;

public interface AuthService {

	LoginResponseDTO login(LoginRequestDTO request);

	LoginResponseDTO refreshToken(String refreshToken);

	RegisterResponseDTO registerUser(RegisterRequestDTO request);

	void setUserPassword(@Valid SetPasswordRequestDTO request);

	void changeUserPassword(@Valid ChangePasswordRequestDTO request, String email);

	UserInfoResponseDTO getLoggedInUser(String email);

}
