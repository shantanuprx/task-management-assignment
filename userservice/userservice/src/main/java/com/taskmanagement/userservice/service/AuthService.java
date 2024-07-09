package com.taskmanagement.userservice.service;

import java.util.Map;

import org.springframework.http.ResponseEntity;

public interface AuthService<T> {

	public ResponseEntity<T> doAction(Map<String, Object> requestMap) throws Exception;
	
}
