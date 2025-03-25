package pl.mk.taskmanager.project.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;
import pl.mk.taskmanager.project.model.ProjectStatus;

public record ProjectUpdateDTO(
    @NotBlank @Size(max = 100) String name,
    @Size(max = 500) String description,
    LocalDateTime deadline,
    @NotNull ProjectStatus status) {}
