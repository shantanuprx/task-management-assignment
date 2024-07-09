package com.taskmanagement.taskcreationservice.service;

import java.util.Map;

import org.springframework.http.ResponseEntity;

public abstract class TaskService<T> {

	
	public abstract ResponseEntity<T> createTask( Map<String, Object> requestMap) throws Exception;

	public abstract ResponseEntity<T> getTaskDetails(Integer taskId) throws Exception;

	public abstract ResponseEntity<T> getTaskDetails(int pageNumber, int pageSize) throws Exception;

	public abstract ResponseEntity<T> updateTask( Integer taskId, Map<String, Object> requestMap) throws Exception;

	public abstract ResponseEntity<T> assignTaskToUser( Integer taskId, String userId) throws Exception;
	
	public abstract ResponseEntity<T> markTaskComplete( Integer taskId) throws Exception;

	public abstract ResponseEntity<T> validateDetails(String userId, Integer taskId);
}
