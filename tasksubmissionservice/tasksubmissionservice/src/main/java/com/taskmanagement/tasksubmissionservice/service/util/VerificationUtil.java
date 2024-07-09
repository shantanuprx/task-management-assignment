package com.taskmanagement.tasksubmissionservice.service.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.taskmanagement.tasksubmissionservice.dto.ResponseDto;
import com.taskmanagement.tasksubmissionservice.exception.BadRequestException;
import com.taskmanagement.tasksubmissionservice.feignclient.TaskCreationFeignClient;

@Component
public class VerificationUtil {

	@Autowired
	private TaskCreationFeignClient taskCreationFeignClient;

	public boolean verifyTaskDetails(Integer taskId, String userId) throws Exception {
		ResponseEntity<Object> response = taskCreationFeignClient.getTaskDetails(userId, taskId);
		ResponseDto responseDto = new ObjectMapper().convertValue(response.getBody(), ResponseDto.class);
		if (responseDto.getMessage() != null) {
			throw new BadRequestException(responseDto.getMessage());
		} else {
			return true;
		}
	}
}
