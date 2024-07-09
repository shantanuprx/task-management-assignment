package com.taskmanagement.userservice.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "TB_USER_PROFILE", schema = "DB_TM")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class UserEntity {

	@Id
	@Column(name = "USER_ID")
	private String userId;
	
	@Column(name = "USER_NAME")
	private String fullName;
	
	@Column(name = "EMAIL_ID")
	private String emailId;
	
	@Column(name = "USER_ROLE")
	private String userRole;
	
	@Column(name = "MOBILE")
	private String mobile;
}
