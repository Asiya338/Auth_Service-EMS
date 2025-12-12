package com.example.demo.exception.handler;

import java.time.LocalDateTime;

import org.slf4j.MDC;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import com.example.demo.dto.res.ErrorResponse;
import com.example.demo.enums.ErrorCodeEnum;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response,
			org.springframework.security.core.AuthenticationException authException)
			throws java.io.IOException, ServletException {
		ErrorResponse error = new ErrorResponse();
		error.setErrorCode(ErrorCodeEnum.INVALID_CREDENTIALS.name());
		error.setErrorMessage("Invalid or missing authentication token");
		error.setPath(request.getRequestURI());
		error.setTraceId(MDC.get("traceId"));
		error.setTimeStamp(LocalDateTime.now());
		error.setHttpMethod(request.getMethod());

		response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		response.setContentType("application/json");
		response.getWriter().write(new ObjectMapper().writeValueAsString(error));
	}
}
