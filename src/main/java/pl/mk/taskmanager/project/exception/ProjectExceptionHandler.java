package pl.mk.taskmanager.project.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import pl.mk.taskmanager.common.exception.ErrorResponse;

@ControllerAdvice(basePackages = "pl.mk.taskmanager.project")
public class ProjectExceptionHandler {

  @ExceptionHandler(ProjectNotFoundException.class)
  public ResponseEntity<ErrorResponse> handleNotFound(ProjectNotFoundException ex) {
    var error =
        ErrorResponse.of(HttpStatus.NOT_FOUND.value(), "Project Not Found", ex.getMessage());
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
  }

  @ExceptionHandler(ProjectAlreadyExistsException.class)
  public ResponseEntity<ErrorResponse> handleAlreadyExists(ProjectAlreadyExistsException ex) {
    var error =
        ErrorResponse.of(HttpStatus.CONFLICT.value(), "Project already exists", ex.getMessage());
    return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
  }
}
