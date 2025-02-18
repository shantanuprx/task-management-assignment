package com.taskmanagement.tasksubmissionservice.service.impl;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.taskmanagement.tasksubmissionservice.adapter.SubmissionAdapter;
import com.taskmanagement.tasksubmissionservice.common.Constants;
import com.taskmanagement.tasksubmissionservice.dto.ErrorDto;
import com.taskmanagement.tasksubmissionservice.dto.ResponseDto;
import com.taskmanagement.tasksubmissionservice.dto.SubmissionDto;
import com.taskmanagement.tasksubmissionservice.dto.SubmissionUpdateDto;
import com.taskmanagement.tasksubmissionservice.entity.SubmissionEntity;
import com.taskmanagement.tasksubmissionservice.repository.TaskSubmissionRepository;
import com.taskmanagement.tasksubmissionservice.service.TaskSubmissionService;
import com.taskmanagement.tasksubmissionservice.service.util.VerificationUtil;

import lombok.extern.slf4j.Slf4j;

@Service("submissions")
@Slf4j
@SuppressWarnings("unchecked")
public class TaskSubmissionServiceImpl<T> extends TaskSubmissionService<T> {

	@Autowired
	private VerificationUtil verificationUtil;

	@Autowired
	private TaskSubmissionRepository repository;

	@Autowired
	private SubmissionAdapter submissionAdapter;

	@Override
	public ResponseEntity<T> createSubmission(Map<String, Object> requestMap) throws Exception {
		log.debug("Entering createSubmission method at {}", System.currentTimeMillis());
		try {
			SubmissionDto submissionDto = new ObjectMapper().convertValue(requestMap, SubmissionDto.class);
			if (verificationUtil.verifyTaskDetails(submissionDto.getTaskId(), submissionDto.getUserId())) {
				SubmissionEntity entity = submissionAdapter.convertModelToEntity(submissionDto);
				repository.save(entity);
				return ResponseEntity.status(HttpStatus.OK)
						.body((T) new ResponseDto(entity.getSubmissionId().toString(), HttpStatus.OK,
								Constants.SUBMISSION_CREATED_SUCCESSFULLY));
			} else {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST)
						.body((T) new ErrorDto(HttpStatus.BAD_REQUEST, "Invalid task details", "Invalid task details", System.currentTimeMillis()));
			}
		} catch (Exception ex) {
			log.error("Exception occurred in createSubmission method with exception ", ex);
			throw ex;
		} finally {
			log.debug("Exiting createSubmission method at {}", System.currentTimeMillis());
		}
	}

	@Override
	public ResponseEntity<T> getSubmissionDetails(Integer submissionId) throws Exception {
		log.debug("Entering getSubmissionDetails method at {}", System.currentTimeMillis());
		try {
			Optional<SubmissionEntity> submissionEntity = repository.findBySubmissionId(submissionId);
			if (submissionEntity.isPresent()) {
				SubmissionDto submissionDto = submissionAdapter.convertEntityToModel(submissionEntity.get());
				return ResponseEntity.status(HttpStatus.OK).body((T) submissionDto);
			} else {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body((T) new ErrorDto(HttpStatus.BAD_REQUEST,
						Constants.TASK_ID_INCORRECT, Constants.TASK_ID_INCORRECT, System.currentTimeMillis()));
			}
		} catch (Exception ex) {
			log.error("Exception occurred in getSubmissionDetails method with exception ", ex);
			throw ex;
		} finally {
			log.debug("Exiting getSubmissionDetails method at {}", System.currentTimeMillis());
		}
	}

	@Override
	public ResponseEntity<T> getSubmissionDetails(int pageNumber, int pageSize) throws Exception {
		log.debug("Entering get all submission details method at {}", System.currentTimeMillis());
		try {
			Page<SubmissionEntity> page = repository.findAll(PageRequest.of(pageNumber, pageSize));
			if (page.hasContent()) {
				List<SubmissionDto> list = page.stream().map(a -> submissionAdapter.convertEntityToModel(a))
						.collect(Collectors.toList());
				return ResponseEntity.status(HttpStatus.OK).body((T) list);
			} else {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body((T) new ErrorDto(HttpStatus.BAD_REQUEST,
						"No content to display", "No content to display", System.currentTimeMillis()));
			}
		} catch (Exception ex) {
			log.error("Exception occurred in get all submission details method with exception ", ex);
			throw ex;
		} finally {
			log.debug("Exiting get all submission details method at {}", System.currentTimeMillis());
		}
	}

	@Override
	public ResponseEntity<T> getSubmissionDetailsByTaskId(Integer taskId, int pageNumber, int pageSize)
			throws Exception {
		log.debug("Entering get all submission details method at {}", System.currentTimeMillis());
		try {
			Page<SubmissionEntity> page = repository.findAllByTaskId(taskId, PageRequest.of(pageNumber, pageSize));
			if (page.hasContent()) {
				List<SubmissionDto> list = page.stream().map(a -> submissionAdapter.convertEntityToModel(a))
						.collect(Collectors.toList());
				return ResponseEntity.status(HttpStatus.OK).body((T) list);
			} else {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body((T) new ErrorDto(HttpStatus.BAD_REQUEST,
						"No content to display", "No content to display", System.currentTimeMillis()));
			}
		} catch (Exception ex) {
			log.error("Exception occurred in get all submission details by task id method with exception ", ex);
			throw ex;
		} finally {
			log.debug("Exiting get all submission details by task id method at {}", System.currentTimeMillis());
		}
	}

	@Override
	public ResponseEntity<T> updateSubmissionDetail(Integer submissionId, Map<String, Object> requestMap) throws Exception {
		log.debug("Entering updateSubmissionDetail method at {}", System.currentTimeMillis());
		try {
			SubmissionUpdateDto submissionDto = new ObjectMapper().convertValue(requestMap, SubmissionUpdateDto.class);
			Optional<SubmissionEntity> submissionEntity = repository.findBySubmissionId(submissionId);
			if (submissionEntity.isPresent()) {
				submissionAdapter.updateEntityFromDto(submissionEntity.get(), submissionDto);
				repository.save(submissionEntity.get());
				return ResponseEntity.status(HttpStatus.OK).body((T) submissionDto);
			} else {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body((T) new ErrorDto(HttpStatus.BAD_REQUEST,
						Constants.TASK_ID_INCORRECT, Constants.TASK_ID_INCORRECT, System.currentTimeMillis()));
			}
		} catch (Exception ex) {
			log.error("Exception occurred in updateSubmissionDetail method with exception ", ex);
			throw ex;
		} finally {
			log.debug("Exiting updateSubmissionDetail method at {}", System.currentTimeMillis());
		}
	}

}
