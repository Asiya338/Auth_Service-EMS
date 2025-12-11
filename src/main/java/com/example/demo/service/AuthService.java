package com.example.demo.service;

import com.example.demo.dto.req.ChangePasswordRequestDTO;
import com.example.demo.dto.req.LoginRequestDTO;
import com.example.demo.dto.req.RegisterRequestDTO;
import com.example.demo.dto.req.SetPasswordRequestDTO;
import com.example.demo.dto.res.LoginResponseDTO;
import com.example.demo.dto.res.RegisterResponseDTO;
import com.example.demo.dto.res.UserInfoResponseDTO;
import com.example.demo.dto.res.ValidateTokenResponseDTO;

import jakarta.validation.Valid;

public interface AuthService {

	LoginResponseDTO login(LoginRequestDTO request);

	LoginResponseDTO refreshToken(String refreshToken);

	RegisterResponseDTO registerUser(RegisterRequestDTO request);

	void setUserPassword(@Valid SetPasswordRequestDTO request);

	void changeUserPassword(@Valid ChangePasswordRequestDTO request, String email);

	UserInfoResponseDTO getLoggedInUser(String email);

	ValidateTokenResponseDTO validateToken(String token);

}
