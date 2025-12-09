package com.example.demo.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginResponseDTO {

	private Long id;

	private String accessToken;
	private String accessType = "Bearer";
	private String email;
	private String role;

	private String refreshToken;
	private int expiryTime;

	private List<String> permissions;
}
