package com.example.demo.dto.req;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ValidateTokenRequestDTO {

	@NotBlank(message = "Token is mandatory")
	private String token;
}
