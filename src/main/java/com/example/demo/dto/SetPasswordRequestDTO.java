package com.example.demo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class SetPasswordRequestDTO {

	@NotBlank(message = "Token is mandatory")
	private String token;

	@NotBlank(message = "Password is mandatory")
	@Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@#$!%*?&_])\\S{8,}$", message = "Password must be at least 8 characters long and include a letter, a number, and a special character")
	private String newPassword;
}
