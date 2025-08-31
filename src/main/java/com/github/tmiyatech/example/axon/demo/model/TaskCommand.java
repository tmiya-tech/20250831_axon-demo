package com.github.tmiyatech.example.axon.demo.model;

import org.axonframework.modelling.command.TargetAggregateIdentifier;

public sealed interface TaskCommand {
  @TargetAggregateIdentifier
  String taskId();

  record CreateTaskCommand(String taskId, String name) implements TaskCommand {}

  record UpdateTaskCommand(String taskId, String name) implements TaskCommand {}

  record CompleteTaskCommand(String taskId) implements TaskCommand {}

  record DeleteTaskCommand(String taskId) implements TaskCommand {}

}
