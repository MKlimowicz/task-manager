package pl.mk.taskmanager.project.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import pl.mk.taskmanager.project.dto.ProjectCreateDTO;
import pl.mk.taskmanager.project.dto.ProjectDTO;
import pl.mk.taskmanager.project.dto.ProjectUpdateDTO;
import pl.mk.taskmanager.project.exception.ProjectAlreadyExistsException;
import pl.mk.taskmanager.project.exception.ProjectNotFoundException;
import pl.mk.taskmanager.project.mapper.ProjectMapper;
import pl.mk.taskmanager.project.model.Project;
import pl.mk.taskmanager.project.model.ProjectStatus;
import pl.mk.taskmanager.project.repository.ProjectRepository;

class ProjectServiceTest {

  @Mock private ProjectRepository projectRepository;

  @InjectMocks private ProjectService projectService;

  private final LocalDateTime now = LocalDateTime.now();

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  void shouldCreateProjectSuccessfully() {
    // given
    ProjectCreateDTO dto = new ProjectCreateDTO("Test", "Desc", now, now.plusDays(10));
    Project projectEntity = ProjectMapper.toEntity(dto);
    Project saved = new Project(1L, "Test", "Desc", now, dto.deadline(), ProjectStatus.PLANNED);

    when(projectRepository.existsByName(dto.name())).thenReturn(false);
    when(projectRepository.save(projectEntity)).thenReturn(saved);

    // when
    ProjectDTO result = projectService.createProject(dto);

    // then
    assertThat(result.id()).isEqualTo(1L);
    assertThat(result.name()).isEqualTo("Test");
    verify(projectRepository).save(projectEntity);
  }

  @Test
  void shouldThrowWhenCreatingDuplicateName() {
    ProjectCreateDTO dto = new ProjectCreateDTO("Dup", "Desc", now, now.plusDays(5));
    when(projectRepository.existsByName(dto.name())).thenReturn(true);

    assertThatThrownBy(() -> projectService.createProject(dto))
        .isInstanceOf(ProjectAlreadyExistsException.class);
  }

  @Test
  void shouldGetProjectById() {
    Project project = new Project(1L, "Test", "Desc", now, now.plusDays(3), ProjectStatus.PLANNED);
    when(projectRepository.findById(1L)).thenReturn(Optional.of(project));

    ProjectDTO result = projectService.getProject(1L);

    assertThat(result.id()).isEqualTo(1L);
    assertThat(result.name()).isEqualTo("Test");
  }

  @Test
  void shouldThrowWhenProjectNotFound() {
    when(projectRepository.findById(42L)).thenReturn(Optional.empty());

    assertThatThrownBy(() -> projectService.getProject(42L))
        .isInstanceOf(ProjectNotFoundException.class);
  }

  @Test
  void shouldReturnAllProjects() {
    List<Project> projects =
        List.of(
            new Project(1L, "A", "desc", now, now.plusDays(1), ProjectStatus.PLANNED),
            new Project(2L, "B", "desc", now, now.plusDays(2), ProjectStatus.IN_PROGRESS));
    when(projectRepository.findAll()).thenReturn(projects);

    List<ProjectDTO> result = projectService.getAllProjects();

    assertThat(result).hasSize(2);
  }

  @Test
  void shouldUpdateProjectSuccessfully() {
    Project existing =
        new Project(
            1L, "Old", "Old desc", now, now.plusDays(2), now.plusDays(3), ProjectStatus.PLANNED);
    ProjectUpdateDTO updateDTO =
        new ProjectUpdateDTO("New", "New desc", now.plusDays(10), ProjectStatus.IN_PROGRESS);

    when(projectRepository.findById(1L)).thenReturn(Optional.of(existing));
    when(projectRepository.existsByName(updateDTO.name())).thenReturn(false);
    when(projectRepository.save(any(Project.class))).thenAnswer(inv -> inv.getArgument(0));

    ProjectDTO result = projectService.updateProject(1L, updateDTO);

    assertThat(result.name()).isEqualTo("New");
    assertThat(result.status()).isEqualTo(ProjectStatus.IN_PROGRESS);
  }

  @Test
  void shouldThrowWhenUpdatingToDuplicateName() {
    Project existing =
        new Project(1L, "Original", "desc", now, now.plusDays(3), ProjectStatus.PLANNED);
    ProjectUpdateDTO updateDTO =
        new ProjectUpdateDTO("Taken", "desc", now.plusDays(3), ProjectStatus.PLANNED);

    when(projectRepository.findById(1L)).thenReturn(Optional.of(existing));
    when(projectRepository.existsByName("Taken")).thenReturn(true);

    assertThatThrownBy(() -> projectService.updateProject(1L, updateDTO))
        .isInstanceOf(ProjectAlreadyExistsException.class);
  }

  @Test
  void shouldDeleteProjectSuccessfully() {
    Project project =
        new Project(1L, "ToDelete", "desc", now, now.plusDays(5), ProjectStatus.PLANNED);
    when(projectRepository.findById(1L)).thenReturn(Optional.of(project));

    projectService.deleteProject(1L);

    verify(projectRepository).delete(project);
  }

  @Test
  void shouldThrowWhenDeletingNonexistentProject() {
    when(projectRepository.findById(999L)).thenReturn(Optional.empty());

    assertThatThrownBy(() -> projectService.deleteProject(999L))
        .isInstanceOf(ProjectNotFoundException.class);
  }
}
