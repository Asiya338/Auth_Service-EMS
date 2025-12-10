package com.example.demo.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RegisterRequestDTO {

	@NotBlank(message = "User Name is mandatory")
	private String username;

	@Email
	@NotBlank(message = "Email is mandatory")
	private String email;

	private String password;

	private String role;
}
