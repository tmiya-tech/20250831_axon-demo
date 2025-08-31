package com.github.tmiyatech.example.axon.demo.controller;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.tmiyatech.example.axon.demo.read.TaskEntity;
import com.github.tmiyatech.example.axon.demo.service.TaskService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/tasks")
@RequiredArgsConstructor
public class TaskController {

  private final TaskService taskService;

  @GetMapping
  public List<TaskEntity> getAllTasks() {
    return taskService.getAllTasks();
  }

  @PostMapping
  public TaskResult createTask(@RequestBody TaskForm taskForm) {
    var taskId = taskService.createTask(taskForm.name());
    return new TaskResult(taskId);
  }

  @GetMapping("/{taskId}")
  public TaskEntity getTask(@PathVariable String taskId) {
    return taskService.getTask(taskId);
  }

  @PutMapping("/{taskId}")
  public TaskResult updateTask(@PathVariable String taskId, @RequestBody TaskForm taskForm) {
    taskService.updateTask(taskId, taskForm.name());
    return new TaskResult(taskId);
  }

  @PostMapping("/{taskId}/complete")
  public TaskResult completeTask(@PathVariable String taskId) {
    taskService.completeTask(taskId);
    return new TaskResult(taskId);
  }

  @DeleteMapping("/{taskId}")
  public TaskResult deleteTask(@PathVariable String taskId) {
    taskService.deleteTask(taskId);
    return new TaskResult(taskId);
  }

}
