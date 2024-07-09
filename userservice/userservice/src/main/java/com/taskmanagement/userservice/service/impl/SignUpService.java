package com.taskmanagement.userservice.service.impl;

import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.taskmanagement.userservice.adapter.ProfileAdapter;
import com.taskmanagement.userservice.constants.Constants;
import com.taskmanagement.userservice.dto.ErrorDto;
import com.taskmanagement.userservice.dto.ProfileDetailDto;
import com.taskmanagement.userservice.dto.ResponseDto;
import com.taskmanagement.userservice.entity.UserEntity;
import com.taskmanagement.userservice.repository.UserProfileRepository;
import com.taskmanagement.userservice.service.AuthService;
import com.taskmanagement.userservice.util.KeyCloakUtil;

import lombok.extern.slf4j.Slf4j;

@Component("signup")
@SuppressWarnings("unchecked")
@Slf4j
public class SignUpService<T> implements AuthService<T> {

	@Autowired
	private ProfileAdapter profileAdapter;

	@Autowired
	private UserProfileRepository profileRepository;

	@Autowired
	private KeyCloakUtil cloakUtil;

	@Override
	public ResponseEntity<T> doAction(Map<String, Object> requestMap) throws Exception {
		log.debug("Entering doAction method at {}", System.currentTimeMillis());
		try {
			ProfileDetailDto profileDetailDto = new ObjectMapper().convertValue(requestMap, ProfileDetailDto.class);
			Optional<UserEntity> entity = profileRepository.findByUserId(profileDetailDto.getUserId());
			if (entity.isPresent()) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body((T) new ErrorDto(HttpStatus.BAD_REQUEST,
						"User id already exists", "User id already exists", System.currentTimeMillis()));
			}
			UserEntity userEntity = profileAdapter.convertModelToEntity(profileDetailDto);
			profileRepository.save(userEntity);
			cloakUtil.addUserToRealm(profileDetailDto);
			return ResponseEntity.status(HttpStatus.OK).body(
					(T) new ResponseDto(userEntity.getUserId(), HttpStatus.OK, Constants.USER_REGISTERD_SUCCESSFULLY));
		} catch (Exception ex) {
			log.debug("Exception occured in doAction method with exception", ex);
			throw ex;
		} finally {
			log.debug("Exiting doAction method at {}", System.currentTimeMillis());
		}
	}

}
