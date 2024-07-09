package com.taskmanagement.userservice.service.impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.taskmanagement.userservice.adapter.ProfileAdapter;
import com.taskmanagement.userservice.constants.Constants;
import com.taskmanagement.userservice.dto.ProfileDetailDto;
import com.taskmanagement.userservice.dto.ResponseDto;
import com.taskmanagement.userservice.entity.UserEntity;
import com.taskmanagement.userservice.repository.UserProfileRepository;
import com.taskmanagement.userservice.service.ProfileService;
import com.taskmanagement.userservice.util.HashUtil;

import lombok.extern.slf4j.Slf4j;

@SuppressWarnings("unchecked")
@Service("users")
@Slf4j
public class ProfileServiceImpl<T> implements ProfileService<T> {

	@Autowired
	private UserProfileRepository profileRepository;

	@Autowired
	private ProfileAdapter profileAdapter;

	@Override
	public ResponseEntity<T> getDetails(String token) throws Exception {
		try {
			log.debug("Entering getDetails with token method at {}", System.currentTimeMillis());
			String userName = HashUtil.getUserNameFromJWTToken(token);
			return getDetailsWithUserName(userName);
		} catch (Exception ex) {
			log.error("Exception occurred in getDetails with token method ", ex);
			throw ex;
		} finally {
			log.debug("Exiting getDetails with token method at {}", System.currentTimeMillis());
		}
	}

	@Override
	public ResponseEntity<T> getDetailsWithUserName(String profileId) throws Exception {
		try {
			log.debug("Entering getDetails with profileId method at {}", System.currentTimeMillis());
			Optional<UserEntity> userEntity = profileRepository.findByUserId(profileId);
			if (userEntity.isEmpty()) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST)
						.body((T) new ResponseDto(profileId, HttpStatus.OK, Constants.USER_DOESNT_EXIST));
			} else {
				return ResponseEntity.status(HttpStatus.OK)
						.body((T) profileAdapter.convertEntityToModel(userEntity.get()));
			}
		} catch (Exception ex) {
			log.error("Exception occurred in getDetails with profileId method ", ex);
			throw ex;
		} finally {
			log.debug("Exiting getDetails with profileId method at {}", System.currentTimeMillis());
		}
	}

	@Override
	public ResponseEntity<T> getAllDetails(int pageNumber, int pageSize) throws Exception {
		try {
			log.debug("Entering getAllDetails method at {}", System.currentTimeMillis());
			Page<UserEntity> currentPage = profileRepository.findAll(PageRequest.of(pageNumber, pageSize));
			List<ProfileDetailDto> result = currentPage.get().map(profileAdapter::convertEntityToModel)
					.collect(Collectors.toList());
			return ResponseEntity.status(HttpStatus.OK).body((T) result);
		} catch (Exception ex) {
			log.error("Exception occurred in getAllDetails method ", ex);
			throw ex;
		} finally {
			log.debug("Exiting getAllDetails method at {}", System.currentTimeMillis());
		}
	}

}
