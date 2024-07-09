package com.taskmanagement.tasksubmissionservice.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.taskmanagement.tasksubmissionservice.entity.SubmissionEntity;

public interface TaskSubmissionRepository extends JpaRepository<SubmissionEntity, Integer> {

	Optional<SubmissionEntity> findBySubmissionId(Integer submissionId);
	
	Page<SubmissionEntity> findAllByTaskId(Integer taskId, Pageable pageable);
}
