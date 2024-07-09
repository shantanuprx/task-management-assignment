package com.taskmanagement.taskcreationservice.adapter;

import java.util.Date;

import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import com.taskmanagement.taskcreationservice.dto.TaskDto;
import com.taskmanagement.taskcreationservice.dto.TaskUpdateDto;
import com.taskmanagement.taskcreationservice.entity.TaskEntity;

import jakarta.validation.Valid;

@Component
@Validated
public class TaskAdapter {

	public TaskEntity convertModelToEntity(@Valid TaskDto taskDto) {
		return TaskEntity.builder()
        .assignedUserId(taskDto.getAssignedUserId())
        .createdDate(new Date())
        .deadLineDate(taskDto.getDeadLineDate())
        .description(taskDto.getDescription())
        .status(taskDto.getAssignedUserId()==null?"D":"A")
        .title(taskDto.getTitle())
        .build();
	}

	public TaskDto convertEntityToModel(TaskEntity taskEntity) {
		TaskDto taskDto = new TaskDto();
		taskDto.setAssignedUserId(taskEntity.getAssignedUserId());
		taskDto.setTitle(taskEntity.getTitle());
		taskDto.setDeadLineDate(taskEntity.getDeadLineDate());
		taskDto.setDescription(taskEntity.getDescription());
		taskDto.setTaskId(taskEntity.getTaskId());
		taskDto.setTaskStatus(taskEntity.getStatus());
		return taskDto;
	}

	public void updateEntityFromDto(TaskEntity entityToUpdate, TaskUpdateDto taskDto) {
		if(taskDto.getAssignedUserId()!=null) {
			entityToUpdate.setAssignedUserId(taskDto.getAssignedUserId());
		}
		
		if(!entityToUpdate.getStatus().equals("A")) {
			entityToUpdate.setStatus(taskDto.getAssignedUserId()==null?"D":"A");
		}
		
		if(taskDto.getTitle() != null) {
			entityToUpdate.setTitle(taskDto.getTitle());
		}
		
		if(taskDto.getDescription() != null) {
			entityToUpdate.setDescription(taskDto.getDescription());
		}
		
		if(taskDto.getDeadLineDate() != null) {
			entityToUpdate.setDeadLineDate(taskDto.getDeadLineDate());
		}
		
		entityToUpdate.setUpdatedDate(new Date());
	}
}
