package com.example.demo.dto.res;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserInfoResponseDTO {

	private Long id;

	private String username;
	private String email;

	private Boolean active = true;

	private String role;
	private List<String> permissions;
}
