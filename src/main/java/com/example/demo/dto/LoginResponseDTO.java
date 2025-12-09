package com.example.demo.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LoginResponseDTO {

//	private Long id;

	private String accessToken;
	private String accessType;
	private String email;
	private String role;

	private String refreshToken;
	private Integer expiryTime;

	private List<String> permissions;
}
