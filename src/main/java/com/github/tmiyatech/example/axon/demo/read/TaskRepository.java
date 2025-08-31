package com.github.tmiyatech.example.axon.demo.read;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<TaskEntity, String> {

}
