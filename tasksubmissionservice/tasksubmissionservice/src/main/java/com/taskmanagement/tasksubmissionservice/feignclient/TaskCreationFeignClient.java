package com.taskmanagement.tasksubmissionservice.feignclient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "taskcreationservice", url="http://taskcreationservicelb:8088")
public interface TaskCreationFeignClient {
	
	@GetMapping("/api/tasks/validate/{userId}/{taskId}")
	public ResponseEntity<Object> getTaskDetails(@PathVariable("userId") String userId, @PathVariable("taskId") Integer taskId) throws Exception;
}
