package com.taskmanagement.taskcreationservice.service.util;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.taskmanagement.taskcreationservice.exception.BadRequestException;
import com.taskmanagement.taskcreationservice.feignclients.Userserviceclient;


@Component
public class VerificationUtil {

	@Autowired
	private Userserviceclient userserviceclient;

	public boolean verifyUserDetails(String userId) throws Exception {
		if(userId == null) { return true; }
		ResponseEntity<Object> response = userserviceclient.getProfileForUserId(userId);
		String responseString = response.getBody().toString().replace('=', ':');
		JSONObject jsonObject = new JSONObject(responseString);
		if (jsonObject.has("message")) {
			throw new BadRequestException(jsonObject.getString("message"));
		} else {
			String role = jsonObject.getString("userRole");
			if (role.equals("ADMIN")) {
				throw new BadRequestException("Can't assign a task to admin user");
			} else {
				return true;
			}
		}
	}
}
