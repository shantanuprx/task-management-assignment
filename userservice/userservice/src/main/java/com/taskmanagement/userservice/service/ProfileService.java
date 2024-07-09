package com.taskmanagement.userservice.service;

import org.springframework.http.ResponseEntity;

/* *
 * Base service class that needs to be implemented by each and every service class
 * implementation of this class will be called from controller.
 * 
 * */

public interface ProfileService<T> {

	public ResponseEntity<T> getDetails(String token) throws Exception;

	public ResponseEntity<T> getDetailsWithUserName(String profileId) throws Exception;
	
	public ResponseEntity<T> getAllDetails(int pageNumber, int pageSize) throws Exception;
	
}