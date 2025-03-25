package pl.mk.taskmanager.project.dto;

import java.time.LocalDateTime;
import pl.mk.taskmanager.project.model.ProjectStatus;

public record ProjectDTO(
    Long id,
    String name,
    String description,
    LocalDateTime createdAt,
    LocalDateTime updatedAt,
    LocalDateTime deadline,
    ProjectStatus status) {}
