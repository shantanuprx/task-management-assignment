package com.taskmanagement.taskcreationservice.feignclients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "userservice")
public interface Userserviceclient {

	@GetMapping("/api/users/{userId}")
	public ResponseEntity<Object> getProfileForUserId(@PathVariable("userId") String userId) throws Exception;
}
