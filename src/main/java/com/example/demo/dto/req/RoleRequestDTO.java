package com.example.demo.dto.req;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class RoleRequestDTO {

	private Long userId;

	@NotBlank(message = "Role is mandatoryto assign to USER")
	private String role;

}
