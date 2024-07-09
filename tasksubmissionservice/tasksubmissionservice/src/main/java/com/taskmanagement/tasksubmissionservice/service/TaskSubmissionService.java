package com.taskmanagement.tasksubmissionservice.service;

import java.util.Map;

import org.springframework.http.ResponseEntity;

public abstract class TaskSubmissionService<T> {

	public abstract ResponseEntity<T> createSubmission(Map<String, Object> requestMap) throws Exception;

	public abstract ResponseEntity<T> getSubmissionDetails(Integer taskId) throws Exception;

	public abstract ResponseEntity<T> getSubmissionDetails(int pageNumber, int pageSize) throws Exception;

	public abstract ResponseEntity<T> getSubmissionDetailsByTaskId(Integer taskId, int pageNumber, int pageSize) throws Exception;

	public abstract ResponseEntity<T> updateSubmissionDetail(Integer taskId, Map<String, Object> requestMap)
			throws Exception;

}
