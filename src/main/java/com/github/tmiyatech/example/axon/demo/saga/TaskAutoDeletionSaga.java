package com.github.tmiyatech.example.axon.demo.saga;

import java.time.Duration;

import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.deadline.DeadlineManager;
import org.axonframework.deadline.annotation.DeadlineHandler;
import org.axonframework.modelling.saga.EndSaga;
import org.axonframework.modelling.saga.SagaEventHandler;
import org.axonframework.modelling.saga.StartSaga;
import org.axonframework.spring.stereotype.Saga;
import org.springframework.beans.factory.annotation.Autowired;

import com.github.tmiyatech.example.axon.demo.model.TaskCommand;
import com.github.tmiyatech.example.axon.demo.model.TaskEvent;

import lombok.extern.slf4j.Slf4j;

@Saga
@Slf4j
public class TaskAutoDeletionSaga {

  @Autowired
  private transient DeadlineManager deadlineManager;
  @Autowired
  private transient CommandGateway commandGateway;

  private String taskId;
  private String deadlineId;

  @StartSaga
  @SagaEventHandler(associationProperty = "taskId")
  public void on(TaskEvent.TaskCreatedEvent event) {
    log.info("Starting TaskAutoDeletionSaga for taskId: {}", event.taskId());
    this.taskId = event.taskId();
  }

  @SagaEventHandler(associationProperty = "taskId")
  public void on(TaskEvent.TaskCompletedEvent event) {
    log.info("Scheduling auto-deletion for taskId: {}", event.taskId());
    this.deadlineId = this.deadlineManager.schedule(Duration.ofMinutes(5), "auto-deletion");
  }

  @DeadlineHandler(deadlineName = "auto-deletion")
  public void onAutoDeletion() {
    log.info("Auto-deleting taskId: {}", taskId);
    this.deadlineId = null;
    this.commandGateway.send(new TaskCommand.DeleteTaskCommand(this.taskId));
  }

  @EndSaga
  @SagaEventHandler(associationProperty = "taskId")
  public void on(TaskEvent.TaskDeletedEvent event) {
    log.info("Ending TaskAutoDeletionSaga for taskId: {}", event.taskId());
    if (this.deadlineId != null) {
      log.info("Cancelling scheduled auto-deletion for taskId: {}", event.taskId());
      this.deadlineManager.cancelSchedule("auto-deletion", this.deadlineId);
    }
  }

}
