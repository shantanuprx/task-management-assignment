package com.taskmanagement.userservice.dto;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Generic Response DTO class
 */
@Data
@AllArgsConstructor
public class ResponseDto {

	private String userId;
	
	private HttpStatus status;
	
	private String message;
}