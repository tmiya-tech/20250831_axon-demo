package com.github.tmiyatech.example.axon.demo.model;

public sealed interface TaskEvent {
  String taskId();

  record TaskCreatedEvent(String taskId, String name) implements TaskEvent {}

  record TaskUpdatedEvent(String taskId, String name) implements TaskEvent {}

  record TaskCompletedEvent(String taskId) implements TaskEvent {}

  record TaskDeletedEvent(String taskId) implements TaskEvent {}

}
