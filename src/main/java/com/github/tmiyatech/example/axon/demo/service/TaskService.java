package com.github.tmiyatech.example.axon.demo.service;

import java.util.List;
import java.util.UUID;

import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.github.tmiyatech.example.axon.demo.EntityNotFoundException;
import com.github.tmiyatech.example.axon.demo.model.TaskCommand;
import com.github.tmiyatech.example.axon.demo.read.TaskEntity;
import com.github.tmiyatech.example.axon.demo.read.TaskRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TaskService {

  private final CommandGateway commandGateway;
  private final TaskRepository taskRepository;

  public String createTask(String name) {
    var taskId = UUID.randomUUID().toString();
    commandGateway.send(new TaskCommand.CreateTaskCommand(taskId, name));
    return taskId;
  }

  public void updateTask(String taskId, String name) {
    if (!taskRepository.existsById(taskId)) {
      throw new EntityNotFoundException("Task not found");
    }

    commandGateway.sendAndWait(new TaskCommand.UpdateTaskCommand(taskId, name));
  }

  public void completeTask(String taskId) {
    if (!taskRepository.existsById(taskId)) {
      throw new EntityNotFoundException("Task not found");
    }

    commandGateway.sendAndWait(new TaskCommand.CompleteTaskCommand(taskId));
  }

  public void deleteTask(String taskId) {
    if (!taskRepository.existsById(taskId)) {
      throw new EntityNotFoundException("Task not found");
    }

    commandGateway.sendAndWait(new TaskCommand.DeleteTaskCommand(taskId));
  }

  public List<TaskEntity> getAllTasks() {
    return taskRepository.findAll(Sort.by(Sort.Order.desc("createdAt")));
  }

  public TaskEntity getTask(String taskId) {
    return taskRepository.findById(taskId)
        .orElseThrow(() -> new EntityNotFoundException("Task not found"));
  }

}
