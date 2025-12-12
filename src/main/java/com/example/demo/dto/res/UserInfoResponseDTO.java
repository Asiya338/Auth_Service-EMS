package com.example.demo.dto.res;

import java.time.LocalDateTime;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserInfoResponseDTO {

	private Long id;

	private String username;
	private String email;

	private Boolean active = true;

	private String role;
	private List<String> permissions;

	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
}
