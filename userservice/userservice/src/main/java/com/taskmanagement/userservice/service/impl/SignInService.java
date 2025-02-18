package com.taskmanagement.userservice.service.impl;

import java.util.Map;
import java.util.Optional;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.taskmanagement.userservice.dto.AuthRequestDto;
import com.taskmanagement.userservice.dto.ErrorDto;
import com.taskmanagement.userservice.dto.TokenDto;
import com.taskmanagement.userservice.entity.UserEntity;
import com.taskmanagement.userservice.repository.UserProfileRepository;
import com.taskmanagement.userservice.service.AuthService;
import com.taskmanagement.userservice.util.KeyCloakUtil;

import lombok.extern.slf4j.Slf4j;

@Component("signin")
@Slf4j
@SuppressWarnings("unchecked")
public class SignInService<T> implements AuthService<T> {

	@Autowired
	private KeyCloakUtil cloakUtil;

	@Autowired
	private UserProfileRepository userProfileRepository;

	@Override
	public ResponseEntity<T> doAction(Map<String, Object> requestMap) throws Exception {
		log.debug("Entering doAction method at {}", System.currentTimeMillis());
		try {
			AuthRequestDto authRequestDto = new ObjectMapper().convertValue(requestMap, AuthRequestDto.class);
			Optional<UserEntity> entity = userProfileRepository.findByUserId(authRequestDto.getUserName());
			if (entity.isPresent()) {
				String token = cloakUtil.verifyUserFromKeyCloak(authRequestDto);
				return ResponseEntity.status(HttpStatus.OK).body((T) new TokenDto(token));
			} else {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body((T) new ErrorDto(HttpStatus.BAD_REQUEST,
						"Invalid user id", "Invalid user id", System.currentTimeMillis()));
			}
		} catch (Exception ex) {
			log.debug("Exception occured in doAction method with exception", ex);
			throw ex;
		} finally {
			log.debug("Exiting doAction method at {}", System.currentTimeMillis());
		}
	}
}
