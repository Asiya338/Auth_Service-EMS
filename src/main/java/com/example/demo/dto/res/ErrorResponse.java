package com.example.demo.dto.res;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ErrorResponse {

	private String errorCode;
	private String errorMessage;
	private String path;
	private String traceId;

	private LocalDateTime timeStamp;

	private String httpMethod;
}
