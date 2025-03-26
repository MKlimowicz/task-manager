package pl.mk.taskmanager.project.service;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.annotation.Transactional;
import pl.mk.taskmanager.common.integration.PostgresTestContainerConfig;
import pl.mk.taskmanager.project.dto.ProjectCreateDTO;
import pl.mk.taskmanager.project.dto.ProjectDTO;
import pl.mk.taskmanager.project.dto.ProjectUpdateDTO;
import pl.mk.taskmanager.project.exception.ProjectAlreadyExistsException;
import pl.mk.taskmanager.project.exception.ProjectNotFoundException;
import pl.mk.taskmanager.project.model.ProjectStatus;
import pl.mk.taskmanager.project.repository.ProjectRepository;

@SpringBootTest
@ContextConfiguration(classes = {PostgresTestContainerConfig.class})
@TestInstance(TestInstance.Lifecycle.PER_METHOD)
@Transactional
class ProjectServiceIT {

  @Autowired private ProjectService projectService;
  @Autowired private ProjectRepository projectRepository;

  private final LocalDateTime now = LocalDateTime.now();

  @BeforeEach
  void cleanDatabase() {
    projectRepository.deleteAll();
  }

  @Test
  void shouldCreateProject() {
    // given
    var dto = new ProjectCreateDTO("Test 1", "desc", now, now.plusDays(3));

    // when
    ProjectDTO result = projectService.createProject(dto);

    // then
    assertThat(result.id()).isNotNull();
    assertThat(result.name()).isEqualTo("Test 1");
  }

  @Test
  void shouldThrowWhenNameAlreadyExists() {
    // given
    var original = new ProjectCreateDTO("Dup", "desc", now, now.plusDays(5));
    var duplicate = new ProjectCreateDTO("Dup", "other", now, now.plusDays(10));
    projectService.createProject(original);
    // when
    // then
    assertThatThrownBy(() -> projectService.createProject(duplicate))
        .isInstanceOf(ProjectAlreadyExistsException.class);
  }

  @Test
  void shouldReturnAllProjects() {
    projectService.createProject(new ProjectCreateDTO("A", "desc", now, now.plusDays(1)));
    projectService.createProject(new ProjectCreateDTO("B", "desc", now, now.plusDays(2)));

    List<ProjectDTO> result = projectService.getAllProjects();

    assertThat(result).hasSize(2);
  }

  @Test
  void shouldReturnSingleProjectById() {
    ProjectDTO saved =
        projectService.createProject(new ProjectCreateDTO("Single", "only", now, now.plusDays(1)));

    ProjectDTO found = projectService.getProject(saved.id());

    assertThat(found.name()).isEqualTo("Single");
  }

  @Test
  void shouldThrowWhenProjectNotFound() {
    assertThatThrownBy(() -> projectService.getProject(999L))
        .isInstanceOf(ProjectNotFoundException.class);
  }

  @Test
  void shouldUpdateProject() {
    ProjectDTO saved =
        projectService.createProject(
            new ProjectCreateDTO("ToUpdate", "desc", now, now.plusDays(5)));

    ProjectUpdateDTO update =
        new ProjectUpdateDTO("Updated", "new", now.plusDays(2), ProjectStatus.COMPLETED);

    ProjectDTO updated = projectService.updateProject(saved.id(), update);

    assertThat(updated.name()).isEqualTo("Updated");
    assertThat(updated.status()).isEqualTo(ProjectStatus.COMPLETED);
  }

  @Test
  void shouldThrowWhenUpdatingToDuplicateName() {
    projectService.createProject(new ProjectCreateDTO("A", "desc", now, now.plusDays(1)));
    ProjectDTO projectB =
        projectService.createProject(new ProjectCreateDTO("B", "desc", now, now.plusDays(2)));

    ProjectUpdateDTO update =
        new ProjectUpdateDTO("A", "new", now.plusDays(3), ProjectStatus.IN_PROGRESS);

    assertThatThrownBy(() -> projectService.updateProject(projectB.id(), update))
        .isInstanceOf(ProjectAlreadyExistsException.class);
  }

  @Test
  void shouldDeleteProject() {
    ProjectDTO project =
        projectService.createProject(
            new ProjectCreateDTO("ToDelete", "desc", now, now.plusDays(1)));

    projectService.deleteProject(project.id());

    assertThatThrownBy(() -> projectService.getProject(project.id()))
        .isInstanceOf(ProjectNotFoundException.class);
  }
}
