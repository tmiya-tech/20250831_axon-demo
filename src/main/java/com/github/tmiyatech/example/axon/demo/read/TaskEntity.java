package com.github.tmiyatech.example.axon.demo.read;

import java.time.Instant;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class TaskEntity {
  @Id
  private String id;
  private String name;
  private boolean completed;
  private String deleted;
  private Instant createdAt;
  private Instant updatedAt;
}
