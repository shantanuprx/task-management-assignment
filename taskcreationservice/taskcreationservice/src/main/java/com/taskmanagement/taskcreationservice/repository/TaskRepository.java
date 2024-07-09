package com.taskmanagement.taskcreationservice.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.taskmanagement.taskcreationservice.entity.TaskEntity;

public interface TaskRepository extends JpaRepository<TaskEntity, Integer> {

	Optional<TaskEntity> findByTaskId(Integer taskId);
}
