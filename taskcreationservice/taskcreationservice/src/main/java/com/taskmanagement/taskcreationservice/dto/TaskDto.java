package com.taskmanagement.taskcreationservice.dto;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.annotation.Nonnull;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class TaskDto {

	
	private Integer taskId;
	
	@Nonnull
	@Size(max = 100, min = 10)
	private String title;

	@Nonnull
	@Size(max = 255, min = 10)
	private String description;

	@Size(max = 100)
	private String assignedUserId;

	@Nonnull
	@FutureOrPresent
	@JsonFormat(pattern = "dd/MM/yyyy")
	private Date deadLineDate;
	
	private String taskStatus;
	
}
