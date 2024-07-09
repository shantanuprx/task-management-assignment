package com.taskmanagement.userservice.adapter;

import java.security.NoSuchAlgorithmException;

import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import com.taskmanagement.userservice.dto.ProfileDetailDto;
import com.taskmanagement.userservice.entity.UserEntity;

import jakarta.validation.Valid;

@Component
@Validated
public class ProfileAdapter {

	public UserEntity convertModelToEntity(@Valid ProfileDetailDto profileDetailDto) throws NoSuchAlgorithmException {
		UserEntity userEntity = new UserEntity();
		userEntity.setUserId(profileDetailDto.getUserId());
		userEntity.setEmailId(profileDetailDto.getEmailId());
		userEntity.setFullName(profileDetailDto.getFullName());
		userEntity.setMobile(profileDetailDto.getMobileNumber());
		userEntity.setUserRole(profileDetailDto.getUserRole());
		return userEntity;
	}

	public ProfileDetailDto convertEntityToModel(UserEntity userEntity) {
		ProfileDetailDto profileDetailDto = new ProfileDetailDto();
		profileDetailDto.setEmailId(userEntity.getEmailId());
		profileDetailDto.setFullName(userEntity.getFullName());
		profileDetailDto.setMobileNumber(userEntity.getMobile());
		profileDetailDto.setUserRole(userEntity.getUserRole());
		profileDetailDto.setUserId(userEntity.getUserId());
		return profileDetailDto;
	}
}
