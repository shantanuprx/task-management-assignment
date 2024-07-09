package com.taskmanagement.tasksubmissionservice.dto;

import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.AllArgsConstructor;
import lombok.Data;
/**
 * Generic error DTO
 */
@Data
@AllArgsConstructor
@JsonInclude(value = Include.NON_NULL)
public class ErrorDto {

	private HttpStatus status;

	private String message;

	private String trace;

	private long timestamp;

}
