package com.taskmanagement.userservice.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.taskmanagement.userservice.service.AuthService;
import com.taskmanagement.userservice.service.ProfileService;
import com.taskmanagement.userservice.util.ServiceLocator;

@RestController
@SuppressWarnings("unchecked")
public class AppController<T> {

	@Autowired
	private ServiceLocator serviceLocator;

	@PostMapping("/auth/{serviceName}")
	public ResponseEntity<T> authService(@RequestBody Map<String, Object> requestMap,
			@PathVariable("serviceName") String serviceName) throws Exception {
		return ((AuthService<T>) serviceLocator.locateServiceBean(serviceName)).doAction(requestMap);
	}

	@GetMapping("/api/users/profile")
	public ResponseEntity<T> getMyProfile(@RequestHeader("Authorization") String token) throws Exception {
		return ((ProfileService<T>) serviceLocator.locateServiceBean("users")).getDetails(token);
	}

	@GetMapping("/api/users/{userId}")
	public ResponseEntity<T> getProfileForUserId(@PathVariable("userId") String userId) throws Exception {
		return ((ProfileService<T>) serviceLocator.locateServiceBean("users")).getDetailsWithUserName(userId);
	}

	@GetMapping("/api/users")
	public ResponseEntity<T> getAllProfiles(@RequestParam("pageNumber") int pageNumber,
			@RequestParam("pageSize") int pageSize) throws Exception {
		return ((ProfileService<T>) serviceLocator.locateServiceBean("users")).getAllDetails(pageNumber, pageSize);
	}
	
	@GetMapping("/api/users/health")
	public String health() {
		return "User service health is ok";
	}
}
