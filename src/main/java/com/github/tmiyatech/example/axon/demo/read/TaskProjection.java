package com.github.tmiyatech.example.axon.demo.read;

import java.time.Instant;

import org.axonframework.eventhandling.EventHandler;
import org.axonframework.eventhandling.Timestamp;
import org.springframework.stereotype.Component;

import com.github.tmiyatech.example.axon.demo.model.TaskEvent;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class TaskProjection {

  private final TaskRepository taskRepository;

  @EventHandler
  public void on(TaskEvent.TaskCreatedEvent event, @Timestamp Instant timestamp) {
    TaskEntity task = new TaskEntity();
    task.setId(event.taskId());
    task.setName(event.name());
    task.setCompleted(false);
    task.setCreatedAt(timestamp);
    task.setUpdatedAt(timestamp);
    taskRepository.save(task);
  }

  @EventHandler
  public void on(TaskEvent.TaskUpdatedEvent event, @Timestamp Instant timestamp) {
    taskRepository.findById(event.taskId()).ifPresent(task -> {
      task.setName(event.name());
      task.setUpdatedAt(timestamp);
      taskRepository.save(task);
    });
  }

  @EventHandler
  public void on(TaskEvent.TaskCompletedEvent event, @Timestamp Instant timestamp) {
    taskRepository.findById(event.taskId()).ifPresent(task -> {
      task.setCompleted(true);
      task.setUpdatedAt(timestamp);
      taskRepository.save(task);
    });
  }

  @EventHandler
  public void on(TaskEvent.TaskDeletedEvent event) {
    taskRepository.deleteById(event.taskId());
  }

}
