package com.taskmanagement.tasksubmissionservice.dto;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Generic Response DTO class
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ResponseDto {

	private String id;
	
	private HttpStatus status;
	
	private String message;
}
