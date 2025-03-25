package pl.mk.taskmanager.project.service;

import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pl.mk.taskmanager.project.dto.ProjectCreateDTO;
import pl.mk.taskmanager.project.dto.ProjectDTO;
import pl.mk.taskmanager.project.dto.ProjectUpdateDTO;
import pl.mk.taskmanager.project.exception.ProjectAlreadyExistsException;
import pl.mk.taskmanager.project.exception.ProjectNotFoundException;
import pl.mk.taskmanager.project.mapper.ProjectMapper;
import pl.mk.taskmanager.project.model.Project;
import pl.mk.taskmanager.project.repository.ProjectRepository;

@Service
@AllArgsConstructor
public class ProjectService {

  private final ProjectRepository projectRepository;

  public ProjectDTO createProject(ProjectCreateDTO dto) {

    if (projectRepository.existsByName(dto.name())) {
      throw new ProjectAlreadyExistsException(dto.name());
    }

    Project project = ProjectMapper.toEntity(dto);
    Project saved = projectRepository.save(project);
    return ProjectMapper.toDTO(saved);
  }

  public ProjectDTO getProject(Long id) {
    Project project = findProjectById(id);
    return ProjectMapper.toDTO(project);
  }

  public List<ProjectDTO> getAllProjects() {
    return projectRepository.findAll().stream()
        .map(ProjectMapper::toDTO)
        .collect(Collectors.toList());
  }

  public ProjectDTO updateProject(Long id, ProjectUpdateDTO dto) {
    Project project = findProjectById(id);

    if (!project.getName().equals(dto.name()) && projectRepository.existsByName(dto.name())) {
      throw new ProjectAlreadyExistsException(dto.name());
    }

    ProjectMapper.updateEntity(dto, project);
    Project updated = projectRepository.save(project);
    return ProjectMapper.toDTO(updated);
  }

  public void deleteProject(Long id) {
    Project project = findProjectById(id);
    projectRepository.delete(project);
  }

  private Project findProjectById(Long id) {
    return projectRepository.findById(id).orElseThrow(() -> new ProjectNotFoundException(id));
  }
}
