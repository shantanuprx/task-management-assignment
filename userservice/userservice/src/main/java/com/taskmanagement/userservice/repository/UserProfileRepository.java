package com.taskmanagement.userservice.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.taskmanagement.userservice.entity.UserEntity;

public interface UserProfileRepository extends JpaRepository<UserEntity, String> {

	public Optional<UserEntity> findByUserId(String userId);
	
}
