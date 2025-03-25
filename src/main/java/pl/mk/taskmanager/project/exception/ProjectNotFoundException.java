package pl.mk.taskmanager.project.exception;

public class ProjectNotFoundException extends RuntimeException {

  public ProjectNotFoundException(Long id) {
    super("Project with ID " + id + " not found.");
  }

  public ProjectNotFoundException(String message) {
    super(message);
  }
}
