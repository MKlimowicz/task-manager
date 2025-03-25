package pl.mk.taskmanager.project.exception;

public class ProjectAlreadyExistsException extends RuntimeException {
  public ProjectAlreadyExistsException(String name) {
    super("Project with name '" + name + "' already exists.");
  }
}
