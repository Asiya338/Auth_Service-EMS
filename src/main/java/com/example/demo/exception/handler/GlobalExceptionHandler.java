package com.example.demo.exception.handler;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;

import org.slf4j.MDC;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.example.demo.dto.res.ErrorResponse;
import com.example.demo.enums.ErrorCodeEnum;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

	private ErrorResponse buildErrorResponse(String errorCode, String message, HttpServletRequest request) {

		ErrorResponse error = new ErrorResponse();
		error.setErrorCode(errorCode);
		error.setErrorMessage(message);
		error.setPath(request.getRequestURI());
		error.setTraceId(MDC.get("traceId"));
		error.setTimeStamp(LocalDateTime.now());
		error.setHttpMethod(request.getMethod());
		return error;
	}

	// 1. Runtime exceptions (generic business errors)
	@ExceptionHandler(RuntimeException.class)
	public ResponseEntity<ErrorResponse> handleRuntime(RuntimeException ex, HttpServletRequest request) {

		log.error("Runtime Exception: {}", ex.getMessage());

		ErrorResponse error = buildErrorResponse(ErrorCodeEnum.BAD_REQUEST.name(), ex.getMessage(), request);

		return ResponseEntity.badRequest().body(error);
	}

	// 2. Not found exceptions
	@ExceptionHandler(NoSuchElementException.class)
	public ResponseEntity<ErrorResponse> handleNotFound(NoSuchElementException ex, HttpServletRequest request) {

		ErrorResponse error = buildErrorResponse(ErrorCodeEnum.USER_NOT_FOUND.name(), ex.getMessage(), request);

		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
	}

	// 3. DTO validation errors
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ErrorResponse> handleValidation(MethodArgumentNotValidException ex,
			HttpServletRequest request) {

		String message = ex.getBindingResult().getAllErrors().get(0).getDefaultMessage();

		ErrorResponse error = buildErrorResponse(ErrorCodeEnum.VALIDATION_FAILED.name(), message, request);

		return ResponseEntity.badRequest().body(error);
	}

	// 4. Fallback for all other exceptions
	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorResponse> handleGeneral(Exception ex, HttpServletRequest request) {

		log.error("Unhandled Exception: {}", ex.getMessage());

		ErrorResponse error = buildErrorResponse(ErrorCodeEnum.INTERNAL_ERROR.name(), ex.getMessage(), request);

		return ResponseEntity.internalServerError().body(error);
	}
}
