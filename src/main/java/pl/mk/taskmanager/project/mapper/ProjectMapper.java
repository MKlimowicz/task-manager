package pl.mk.taskmanager.project.mapper;

import java.time.LocalDateTime;
import pl.mk.taskmanager.project.dto.ProjectCreateDTO;
import pl.mk.taskmanager.project.dto.ProjectDTO;
import pl.mk.taskmanager.project.dto.ProjectUpdateDTO;
import pl.mk.taskmanager.project.model.Project;
import pl.mk.taskmanager.project.model.ProjectStatus;

public class ProjectMapper {

  public static Project toEntity(ProjectCreateDTO dto) {
    return Project.builder()
        .name(dto.name())
        .description(dto.description())
        .createdAt(dto.createdAt())
        .deadline(dto.deadline())
        .status(ProjectStatus.PLANNED)
        .build();
  }

  public static void updateEntity(ProjectUpdateDTO dto, Project project) {
    project.setName(dto.name());
    project.setDescription(dto.description());
    project.setDeadline(dto.deadline());
    project.setStatus(dto.status());
    project.setUpdatedAt(LocalDateTime.now());
  }

  public static ProjectDTO toDTO(Project project) {
    return new ProjectDTO(
        project.getId(),
        project.getName(),
        project.getDescription(),
        project.getCreatedAt(),
        project.getUpdatedAt(),
        project.getDeadline(),
        project.getStatus());
  }
}
