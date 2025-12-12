package com.example.demo.exception.handler;

import java.io.IOException;
import java.time.LocalDateTime;

import org.slf4j.MDC;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import com.example.demo.dto.res.ErrorResponse;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

	@Override
	public void handle(HttpServletRequest request, HttpServletResponse response,
			AccessDeniedException accessDeniedException) throws IOException, ServletException {

		ErrorResponse eResponse = new ErrorResponse();
		eResponse.setErrorCode("40001");
		eResponse.setErrorMessage("You do not have permission to access this resource");
		eResponse.setPath(request.getRequestURI());
		eResponse.setHttpMethod(request.getMethod());
		eResponse.setTimeStamp(LocalDateTime.now());
		eResponse.setTraceId(MDC.get("traceId"));

		response.setContentType("application/json");
		response.setStatus(HttpServletResponse.SC_FORBIDDEN);
		response.getWriter().write(new ObjectMapper().writeValueAsString(eResponse));

	}

}
