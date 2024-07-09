package com.taskmanagement.taskcreationservice.dto;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

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
public class TaskUpdateDto {

	
	@Size(max = 100, min = 10)
	private String title;

	@Size(max = 255, min = 10)
	private String description;

	@Size(max = 100)
	private String assignedUserId;

	@JsonFormat(pattern = "dd/MM/yyyy")
	private Date deadLineDate;
	
}
