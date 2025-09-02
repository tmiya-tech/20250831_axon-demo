package com.github.tmiyatech.example.axon.demo.model;

import java.time.Duration;

import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.deadline.DeadlineManager;
import org.axonframework.deadline.annotation.DeadlineHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;
import org.springframework.beans.factory.annotation.Autowired;

import lombok.Getter;
import lombok.experimental.Accessors;

@Aggregate
@Getter
@Accessors(fluent = true)
public class TaskAggregate {
  @Autowired
  private transient DeadlineManager deadlineManager;

  @AggregateIdentifier
  private String id;
  private String name;
  private boolean completed;
  private String deleted;
  private String autoDeletionDeadlineId;

  protected TaskAggregate() {
    // Required by Axon Framework
  }

  @CommandHandler
  public TaskAggregate(TaskCommand.CreateTaskCommand command) {
    AggregateLifecycle.apply(new TaskEvent.TaskCreatedEvent(command.taskId(), command.name()));
  }

  @CommandHandler
  public void handle(TaskCommand.UpdateTaskCommand command) {
    AggregateLifecycle.apply(new TaskEvent.TaskUpdatedEvent(command.taskId(), command.name()));
  }

  @CommandHandler
  public void handle(TaskCommand.CompleteTaskCommand command) {
    var autoDeletionDeadlineId = this.deadlineManager.schedule(Duration.ofMinutes(5), "auto-deletion");
    AggregateLifecycle.apply(new TaskEvent.TaskCompletedEvent(command.taskId(), autoDeletionDeadlineId));
  }

  @CommandHandler
  public void handle(TaskCommand.DeleteTaskCommand command) {
    AggregateLifecycle.apply(new TaskEvent.TaskDeletedEvent(command.taskId()));
  }

  @EventSourcingHandler
  public void on(TaskEvent.TaskCreatedEvent event) {
    this.id = event.taskId();
    this.name = event.name();
    this.completed = false;
  }

  @EventSourcingHandler
  public void on(TaskEvent.TaskUpdatedEvent event) {
    this.name = event.name();
  }

  @EventSourcingHandler
  public void on(TaskEvent.TaskCompletedEvent event) {
    this.completed = true;
    this.autoDeletionDeadlineId = event.autoDeletionDeadlineId();
  }

  @DeadlineHandler(deadlineName = "auto-deletion")
  public void onAutoDeletion() {
    this.autoDeletionDeadlineId = null;
    AggregateLifecycle.apply(new TaskEvent.TaskDeletedEvent(this.id));
  }

  @EventSourcingHandler
  public void on(TaskEvent.TaskDeletedEvent event) {
    this.deleted = event.taskId();
    if (this.autoDeletionDeadlineId != null) {
      this.deadlineManager.cancelSchedule("auto-deletion", this.autoDeletionDeadlineId);
      this.autoDeletionDeadlineId = null;
    }
  }
}
