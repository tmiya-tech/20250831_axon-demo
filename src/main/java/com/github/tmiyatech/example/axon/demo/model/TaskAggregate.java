package com.github.tmiyatech.example.axon.demo.model;

import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;

import lombok.Getter;
import lombok.experimental.Accessors;

@Aggregate
@Getter
@Accessors(fluent = true)
public class TaskAggregate {
  @AggregateIdentifier
  private String id;
  private String name;
  private boolean completed;
  private String deleted;

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
    AggregateLifecycle.apply(new TaskEvent.TaskCompletedEvent(command.taskId()));
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
  }

  @EventSourcingHandler
  public void on(TaskEvent.TaskDeletedEvent event) {
    this.deleted = event.taskId();
  }
}
