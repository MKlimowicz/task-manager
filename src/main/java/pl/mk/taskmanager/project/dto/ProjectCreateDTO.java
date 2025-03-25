package pl.mk.taskmanager.project.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;

public record ProjectCreateDTO(
    @NotBlank @Size(max = 100) String name,
    @Size(max = 500) String description,
    LocalDateTime deadline) {}
