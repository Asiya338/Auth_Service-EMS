package com.example.demo.dto.res;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ValidateTokenResponseDTO {

	private Boolean valid;
	private Long userId;
	private String email;

	private String role;
	private List<String> permissions;
}
