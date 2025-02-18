package com.taskmanagement.taskcreationservice.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.taskmanagement.taskcreationservice.service.TaskService;
import com.taskmanagement.taskcreationservice.service.util.ServiceLocator;

@RestController
@RequestMapping("/api/tasks")
@SuppressWarnings("unchecked")
public class ApplicationController<T> {

	@Autowired
	private ServiceLocator serviceLocator;

	@PostMapping
	public ResponseEntity<T> createTask(@RequestBody Map<String, Object> requestMap) throws Exception {
		return ((TaskService<T>) serviceLocator.locateServiceBean("tasks")).createTask(requestMap);
	}

	@GetMapping("/{taskId}")
	public ResponseEntity<T> getTaskDetails(@PathVariable("taskId") Integer taskId) throws Exception {
		return ((TaskService<T>) serviceLocator.locateServiceBean("tasks")).getTaskDetails(taskId);
	}

	@GetMapping
	public ResponseEntity<T> getTaskDetails(@RequestParam("pageNumber") int pageNumber,
			@RequestParam("pageSize") int pageSize) throws Exception {
		return ((TaskService<T>) serviceLocator.locateServiceBean("tasks")).getTaskDetails(pageNumber, pageSize);
	}

	@PutMapping("/{taskId}")
	public ResponseEntity<T> updateTask(@PathVariable("taskId") Integer taskId,
			@RequestBody Map<String, Object> requestMap) throws Exception {
		return ((TaskService<T>) serviceLocator.locateServiceBean("tasks")).updateTask(taskId, requestMap);
	}

	@PutMapping("/{taskId}/user/{userId}/assigned")
	public ResponseEntity<T> assignTaskToUser(@PathVariable("taskId") Integer taskId,
			@PathVariable("userId") String userId) throws Exception {
		return ((TaskService<T>) serviceLocator.locateServiceBean("tasks")).assignTaskToUser(taskId, userId);
	}

	@PutMapping("/{taskId}/complete")
	public ResponseEntity<T> markTaskComplete(@PathVariable("taskId") Integer taskId) throws Exception {
		return ((TaskService<T>) serviceLocator.locateServiceBean("tasks")).markTaskComplete(taskId);
	}
	
	@GetMapping("/validate/{userId}/{taskId}")
	public ResponseEntity<T> getTaskDetails(@PathVariable("userId") String userId, @PathVariable("taskId") Integer taskId) throws Exception {
		return ((TaskService<T>) serviceLocator.locateServiceBean("tasks")).validateDetails(userId, taskId);
	}
}
