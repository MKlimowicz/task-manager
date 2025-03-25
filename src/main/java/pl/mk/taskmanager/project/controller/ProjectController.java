package pl.mk.taskmanager.project.controller;

import jakarta.validation.Valid;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.mk.taskmanager.project.dto.ProjectCreateDTO;
import pl.mk.taskmanager.project.dto.ProjectDTO;
import pl.mk.taskmanager.project.dto.ProjectUpdateDTO;
import pl.mk.taskmanager.project.service.ProjectService;

@RestController
@RequestMapping("/api/projects")
@AllArgsConstructor
public class ProjectController {

  private final ProjectService projectService;

  @PostMapping
  public ResponseEntity<ProjectDTO> createProject(@Valid @RequestBody ProjectCreateDTO dto) {
    ProjectDTO created = projectService.createProject(dto);
    return ResponseEntity.status(201).body(created);
  }

  @GetMapping("/{id}")
  public ResponseEntity<ProjectDTO> getProject(@PathVariable Long id) {
    return ResponseEntity.ok(projectService.getProject(id));
  }

  @GetMapping
  public ResponseEntity<List<ProjectDTO>> getAllProjects() {
    return ResponseEntity.ok(projectService.getAllProjects());
  }

  @PutMapping("/{id}")
  public ResponseEntity<ProjectDTO> updateProject(
      @PathVariable Long id, @Valid @RequestBody ProjectUpdateDTO dto) {
    return ResponseEntity.ok(projectService.updateProject(id, dto));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteProject(@PathVariable Long id) {
    projectService.deleteProject(id);
    return ResponseEntity.noContent().build();
  }
}
