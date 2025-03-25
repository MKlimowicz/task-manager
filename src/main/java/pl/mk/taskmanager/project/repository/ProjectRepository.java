package pl.mk.taskmanager.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.mk.taskmanager.project.model.Project;

public interface ProjectRepository extends JpaRepository<Project, Long> {
  boolean existsByName(String name);
}
