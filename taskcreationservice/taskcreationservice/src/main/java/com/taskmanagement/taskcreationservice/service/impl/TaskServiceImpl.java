package com.taskmanagement.taskcreationservice.service.impl;

import java.util.Date;
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
import com.taskmanagement.taskcreationservice.adapter.TaskAdapter;
import com.taskmanagement.taskcreationservice.common.Constants;
import com.taskmanagement.taskcreationservice.dto.ErrorDto;
import com.taskmanagement.taskcreationservice.dto.ResponseDto;
import com.taskmanagement.taskcreationservice.dto.TaskDto;
import com.taskmanagement.taskcreationservice.dto.TaskUpdateDto;
import com.taskmanagement.taskcreationservice.entity.TaskEntity;
import com.taskmanagement.taskcreationservice.repository.TaskRepository;
import com.taskmanagement.taskcreationservice.service.TaskService;
import com.taskmanagement.taskcreationservice.service.util.VerificationUtil;

import lombok.extern.slf4j.Slf4j;

@Service("tasks")
@Slf4j
@SuppressWarnings("unchecked")
public class TaskServiceImpl<T> extends TaskService<T> {

	@Autowired
	private TaskRepository repository;

	@Autowired
	private TaskAdapter taskAdapter;

	@Autowired
	private VerificationUtil verificationUtil;

	@Override
	public ResponseEntity<T> createTask(Map<String, Object> requestMap) throws Exception {
		log.debug("Entering create Task method at {}", System.currentTimeMillis());
		try {
			TaskDto taskDto = new ObjectMapper().convertValue(requestMap, TaskDto.class);
			TaskEntity taskEntity = taskAdapter.convertModelToEntity(taskDto);
			if (verificationUtil.verifyUserDetails(taskEntity.getAssignedUserId())) {
				repository.save(taskEntity);
				return ResponseEntity.status(HttpStatus.OK)
						.body((T) new ResponseDto(taskEntity.getTaskId().toString(), HttpStatus.OK, "Task created successfully"));
			} else {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body((T) new ErrorDto(HttpStatus.BAD_REQUEST,
						"Assigned user doesn't exist", "Assigned user doesn't exist", System.currentTimeMillis()));
			}
		} catch (Exception ex) {
			log.error("Exception occurred in create Task method with exception ", ex);
			throw ex;
		} finally {
			log.debug("Exiting create Task method at {}", System.currentTimeMillis());
		}
	}

	@Override
	public ResponseEntity<T> getTaskDetails(Integer taskId) throws Exception {
		log.debug("Entering get Task Details with taskId {} method at {}", taskId, System.currentTimeMillis());
		try {
			Optional<TaskEntity> taskEntity = repository.findByTaskId(taskId);
			if (taskEntity.isPresent()) {
				TaskDto taskDto = taskAdapter.convertEntityToModel(taskEntity.get());
				return ResponseEntity.status(HttpStatus.OK).body((T) taskDto);
			} else {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body((T) new ErrorDto(HttpStatus.BAD_REQUEST,
						Constants.TASK_ID_INCORRECT, Constants.TASK_ID_INCORRECT, System.currentTimeMillis()));
			}
		} catch (Exception ex) {
			log.error("Exception occurred in get Task Details with taskId method with exception ", ex);
			throw ex;
		} finally {
			log.debug("Exiting get Task Details with taskId method at {}", System.currentTimeMillis());
		}
	}

	@Override
	public ResponseEntity<T> getTaskDetails(int pageNumber, int pageSize) throws Exception {
		log.debug("Entering get Task Details method at {}", System.currentTimeMillis());
		try {
			Page<TaskEntity> page = repository.findAll(PageRequest.of(pageNumber, pageSize));
			if (page.hasContent()) {
				List<TaskDto> list = page.stream().map(a -> taskAdapter.convertEntityToModel(a))
						.collect(Collectors.toList());
				return ResponseEntity.status(HttpStatus.OK).body((T) list);
			} else {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body((T) new ErrorDto(HttpStatus.BAD_REQUEST,
						"No content to display", "No content to display", System.currentTimeMillis()));
			}
		} catch (Exception ex) {
			log.error("Exception occurred in get Task Details method with exception ", ex);
			throw ex;
		} finally {
			log.debug("Exiting get Task Details method at {}", System.currentTimeMillis());
		}
	}

	@Override
	public ResponseEntity<T> updateTask(Integer taskId, Map<String, Object> requestMap) throws Exception {
		log.debug("Entering updateTask method at {}", System.currentTimeMillis());
		try {
			Optional<TaskEntity> taskEntity = repository.findByTaskId(taskId);
			TaskUpdateDto taskDto = new ObjectMapper().convertValue(requestMap, TaskUpdateDto.class);

			if (taskEntity.isPresent()) {
				if (verificationUtil.verifyUserDetails(taskDto.getAssignedUserId())) {
					TaskEntity entityToUpdate = taskEntity.get();
					taskAdapter.updateEntityFromDto(entityToUpdate, taskDto);
					repository.save(entityToUpdate);
					return ResponseEntity.status(HttpStatus.OK).body((T) new ResponseDto(taskId.toString(),
							HttpStatus.OK, Constants.RECORD_UPDATED_SUCESSFULLY));
				} else {
					return ResponseEntity.status(HttpStatus.BAD_REQUEST).body((T) new ErrorDto(HttpStatus.BAD_REQUEST,
							"Assigned user doesn't exist", "Assigned user doesn't exist", System.currentTimeMillis()));
				}
			} else {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body((T) new ErrorDto(HttpStatus.BAD_REQUEST,
						Constants.TASK_ID_INCORRECT, Constants.TASK_ID_INCORRECT, System.currentTimeMillis()));
			}
		} catch (Exception ex) {
			log.error("Exception occurred in updateTask method with exception ", ex);
			throw ex;
		} finally {
			log.debug("Exiting updateTask method at {}", System.currentTimeMillis());
		}
	}

	@Override
	public ResponseEntity<T> assignTaskToUser(Integer taskId, String userId) throws Exception {
		log.debug("Entering assignTaskToUser method at {}", System.currentTimeMillis());
		try {
			Optional<TaskEntity> taskEntity = repository.findByTaskId(taskId);
			if (taskEntity.isPresent()) {
				if (taskEntity.get().getStatus().equals("C")) {
					return ResponseEntity.status(HttpStatus.BAD_REQUEST)
							.body((T) new ErrorDto(HttpStatus.BAD_REQUEST, Constants.CANT_REASSIGN_COMPLETED_TASK,
									Constants.CANT_REASSIGN_COMPLETED_TASK, System.currentTimeMillis()));
				}
				if (verificationUtil.verifyUserDetails(userId)) {
					TaskEntity entityToUpdate = taskEntity.get();
					entityToUpdate.setAssignedUserId(userId);
					entityToUpdate.setStatus("A");
					entityToUpdate.setUpdatedDate(new Date());
					repository.save(entityToUpdate);
					return ResponseEntity.status(HttpStatus.OK).body((T) new ResponseDto(taskId.toString(),
							HttpStatus.OK, Constants.RECORD_UPDATED_SUCESSFULLY));
				} else {
					return ResponseEntity.status(HttpStatus.BAD_REQUEST).body((T) new ErrorDto(HttpStatus.BAD_REQUEST,
							"Assigned user doesn't exist", "Assigned user doesn't exist", System.currentTimeMillis()));
				}
			} else {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body((T) new ErrorDto(HttpStatus.BAD_REQUEST,
						Constants.TASK_ID_INCORRECT, Constants.TASK_ID_INCORRECT, System.currentTimeMillis()));
			}
		} catch (Exception ex) {
			log.error("Exception occurred in assignTaskToUser method with exception ", ex);
			throw ex;
		} finally {
			log.debug("Exiting assignTaskToUser method at {}", System.currentTimeMillis());
		}
	}

	@Override
	public ResponseEntity<T> markTaskComplete(Integer taskId) throws Exception {
		log.debug("Entering markTaskComplete method at {}", System.currentTimeMillis());
		try {
			Optional<TaskEntity> taskEntity = repository.findByTaskId(taskId);
			if (taskEntity.isPresent()) {
				if (taskEntity.get().getStatus().equals("C")) {
					return ResponseEntity.status(HttpStatus.BAD_REQUEST)
							.body((T) new ErrorDto(HttpStatus.BAD_REQUEST, Constants.CANT_COMPLETE_COMPLETED_TASK,
									Constants.CANT_COMPLETE_COMPLETED_TASK, System.currentTimeMillis()));
				} else {
					TaskEntity entityToUpdate = taskEntity.get();
					entityToUpdate.setStatus("C");
					entityToUpdate.setUpdatedDate(new Date());
					repository.save(entityToUpdate);
					return ResponseEntity.status(HttpStatus.OK).body((T) new ResponseDto(taskId.toString(),
							HttpStatus.OK, Constants.RECORD_UPDATED_SUCESSFULLY));
				}
			} else {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body((T) new ErrorDto(HttpStatus.BAD_REQUEST,
						Constants.TASK_ID_INCORRECT, Constants.TASK_ID_INCORRECT, System.currentTimeMillis()));
			}
		} catch (Exception ex) {
			log.error("Exception occurred in markTaskComplete method with exception ", ex);
			throw ex;
		} finally {
			log.debug("Exiting markTaskComplete method at {}", System.currentTimeMillis());
		}
	}

	@Override
	public ResponseEntity<T> validateDetails(String userId, Integer taskId) {
		log.debug("Entering validateDetails method at {}", System.currentTimeMillis());

		try {
			Optional<TaskEntity> taskEntity = repository.findByTaskId(taskId);
			if (taskEntity.isEmpty()) {
				return ResponseEntity.status(HttpStatus.OK)
						.body((T) new ResponseDto(null, HttpStatus.BAD_REQUEST, Constants.TASK_ID_INCORRECT));
			}
			if (!taskEntity.get().getAssignedUserId().equals(userId)) {
				return ResponseEntity.status(HttpStatus.OK)
						.body((T) new ResponseDto(null, HttpStatus.BAD_REQUEST, "Task not assigned to " + userId));
			}
			if (taskEntity.get().getStatus().equals("C")) {
				return ResponseEntity.status(HttpStatus.OK)
						.body((T) new ResponseDto(null, HttpStatus.BAD_REQUEST, "Task already completed"));
			}
			if (new Date().after(taskEntity.get().getDeadLineDate())) {
				return ResponseEntity.status(HttpStatus.OK)
						.body((T) new ResponseDto(null, HttpStatus.BAD_REQUEST, "Deadline already passed"));
			}
			return ResponseEntity.status(HttpStatus.OK)
					.body((T) new ResponseDto(null, HttpStatus.OK, null));
		} catch (Exception ex) {
			log.error("Exception occurred in validateDetails method with exception ", ex);
			throw ex;
		} finally {
			log.debug("Exiting validateDetails method at {}", System.currentTimeMillis());
		}
	}

}
