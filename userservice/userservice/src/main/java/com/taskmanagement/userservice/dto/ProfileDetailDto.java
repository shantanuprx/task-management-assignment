package com.taskmanagement.userservice.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import jakarta.annotation.Nonnull;
import jakarta.validation.constraints.Pattern;
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
@JsonInclude(value = Include.NON_NULL)
public class ProfileDetailDto {

	@Nonnull
	@Size(max = 100)
	private String userId;
	
	@Nonnull
	@Size(max = 125)
	private String fullName;
	
	@Nonnull
	@Size(max = 100)
	private String emailId;
	
	@Nonnull
	@Size(max = 16)
	private String password;
	
	@Nonnull
	@Size(max = 5)
	private String userRole;
	
	@Nonnull
	@Size(max = 10)
	@Pattern(regexp = "^[6-9]\\d{9}$")
	private String mobileNumber;
	
}
