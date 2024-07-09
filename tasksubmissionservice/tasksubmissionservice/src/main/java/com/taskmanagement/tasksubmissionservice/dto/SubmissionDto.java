package com.taskmanagement.tasksubmissionservice.dto;

import java.util.Date;

import jakarta.annotation.Nonnull;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
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
public class SubmissionDto {
	
	private Integer submissionId;
	
	@NotNull
	@Min(0)
	private Integer taskId;
	
	@Size(max = 100)
	private String userId;
	
	@Nonnull
	@Size(max = 100, min = 10)
	private String title;

	@Nonnull
	@Size(max = 255, min = 10)
	private String description;
	
	@NotNull
	@Size(min = 1, max = 1)
	private String submissionStatus;
	
	private Date submissionDate;
}
