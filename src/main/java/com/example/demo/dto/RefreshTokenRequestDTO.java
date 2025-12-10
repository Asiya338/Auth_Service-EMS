package com.example.demo.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class RefreshTokenRequestDTO {

	@NotBlank(message = "Old Refresh Token or JWT token is mandatory ")
	private String refreshToken;
}
