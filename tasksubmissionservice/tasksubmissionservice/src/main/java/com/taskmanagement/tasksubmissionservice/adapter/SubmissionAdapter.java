package com.taskmanagement.tasksubmissionservice.adapter;

import java.util.Date;

import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import com.taskmanagement.tasksubmissionservice.dto.SubmissionDto;
import com.taskmanagement.tasksubmissionservice.dto.SubmissionUpdateDto;
import com.taskmanagement.tasksubmissionservice.entity.SubmissionEntity;

import jakarta.validation.Valid;

@Component
@Validated
public class SubmissionAdapter {

	public SubmissionEntity convertModelToEntity(@Valid SubmissionDto submissionDto) {
		return SubmissionEntity.builder()
        .createdDate(new Date())
        .description(submissionDto.getDescription())
        .status(submissionDto.getSubmissionStatus())
        .submissionDate(submissionDto.getSubmissionStatus().equals("C")?new Date():null)
        .taskId(submissionDto.getTaskId())
        .title(submissionDto.getTitle())
        .userId(submissionDto.getUserId())
        .build();
	}

	public SubmissionDto convertEntityToModel(SubmissionEntity submissionEntity) {
		SubmissionDto submissionDto = new SubmissionDto();
		submissionDto.setUserId(submissionEntity.getUserId());
		submissionDto.setTitle(submissionEntity.getTitle());
		submissionDto.setSubmissionDate(submissionEntity.getSubmissionDate());
		submissionDto.setDescription(submissionEntity.getDescription());
		submissionDto.setTaskId(submissionEntity.getTaskId());
		submissionDto.setSubmissionStatus(submissionEntity.getStatus());
		return submissionDto;
	}

	public void updateEntityFromDto(SubmissionEntity entityToUpdate, SubmissionUpdateDto submissionDto) {
		if(submissionDto.getSubmissionStatus() != null) {
			entityToUpdate.setStatus(submissionDto.getSubmissionStatus());
			if(submissionDto.getSubmissionStatus().equals("C")) {
				entityToUpdate.setSubmissionDate(new Date());
			}
		}
		
		if(submissionDto.getTitle() != null) {
			entityToUpdate.setTitle(submissionDto.getTitle());
		}
		
		if(submissionDto.getDescription() != null) {
			entityToUpdate.setDescription(submissionDto.getDescription());
		}
		
		entityToUpdate.setUpdatedDate(new Date());
	}
}
