package com.taskmanagement.tasksubmissionservice.service.util;

import java.util.Base64;

import org.json.JSONObject;

import com.taskmanagement.tasksubmissionservice.exception.BadRequestException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class HashUtil {

	public static String getUserNameFromJWTToken(String token) {
		try {
			token = token.split("Bearer ")[1];
			String[] headerAndPayload = token.split("\\.");
			Base64.Decoder decoder = Base64.getUrlDecoder();
			String payload = new String(decoder.decode(headerAndPayload[1]));
			JSONObject payloadJSON = new JSONObject(payload);
			return payloadJSON.optString("preferred_username");
		} catch (Exception ex) {
			log.error("Exception occured in getUserNameFromJWTToken ", ex);
			throw new BadRequestException("Issue with the Authorization token");
		}
	}

}