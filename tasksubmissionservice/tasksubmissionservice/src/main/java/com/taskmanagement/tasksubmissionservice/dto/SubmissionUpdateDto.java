package com.taskmanagement.tasksubmissionservice.dto;

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
public class SubmissionUpdateDto {
	
	@Size(max = 100, min = 10)
	private String title;

	@Size(max = 255, min = 10)
	private String description;

	@Size(max = 1, min = 1)
	private String submissionStatus;
	
}
