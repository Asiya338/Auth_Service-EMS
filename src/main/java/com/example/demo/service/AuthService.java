package com.example.demo.service;

import com.example.demo.dto.LoginRequestDTO;
import com.example.demo.dto.LoginResponseDTO;

public interface AuthService {

	LoginResponseDTO login(LoginRequestDTO request);

	LoginResponseDTO refreshToken(String refreshToken);

}
