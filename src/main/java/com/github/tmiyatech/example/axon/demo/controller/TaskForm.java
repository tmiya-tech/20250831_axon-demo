package com.github.tmiyatech.example.axon.demo.controller;

import jakarta.validation.constraints.NotBlank;

public record TaskForm(
  @NotBlank
  String name) {
}
