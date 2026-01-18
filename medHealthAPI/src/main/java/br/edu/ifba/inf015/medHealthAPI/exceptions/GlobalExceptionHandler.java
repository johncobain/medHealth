package br.edu.ifba.inf015.medHealthAPI.exceptions;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {
  @ExceptionHandler(EntityNotFoundException.class)
  public ResponseEntity<Map<String,String>> handleEntityNotFoundException(EntityNotFoundException ex, HttpServletRequest request){
    Map<String, String> error = new HashMap<>();
    error.put("status", ex.getStatusCode().toString());
    error.put("reason", ex.getReason());
    error.put("path", request.getRequestURI());
    return ResponseEntity.status(ex.getStatusCode()).body(error);
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<Map<String,Object>> handleValidationExceptions(MethodArgumentNotValidException ex, HttpServletRequest request){
    Map<String, Object> response = new HashMap<>();
    Map<String, String> errors = new HashMap<>();

    ex.getBindingResult().getAllErrors().forEach((error) -> {
      String fieldName = ((FieldError) error).getField();
      String errorMessage = error.getDefaultMessage();
      errors.put(fieldName, errorMessage);
    });

    response.put("status", ex.getStatusCode().toString());
    response.put("reason", "Validation failed");
    response.put("path", request.getRequestURI());
    response.put("errors", errors);

      return ResponseEntity.status(ex.getStatusCode()).body(response);
  }

  @ExceptionHandler(UniqueAttributeAlreadyRegisteredException.class)
  public ResponseEntity<Map<String, String>> handleUniqueAttributeAlreadyRegisteredException(UniqueAttributeAlreadyRegisteredException ex, HttpServletRequest request){
    Map<String, String> error = new HashMap<>();
    error.put("status", ex.getStatusCode().toString());
    error.put("reason", ex.getReason());
    error.put("path", request.getRequestURI());
    return ResponseEntity.status(ex.getStatusCode()).body(error);
  }

  @ExceptionHandler(NoResourceFoundException.class)
  public ResponseEntity<Map<String, String>> handleNoResourceFoundException(NoResourceFoundException ex, HttpServletRequest request){
    Map<String, String> error = new HashMap<>();
    error.put("status", ex.getStatusCode().toString());
    error.put("reason", "Resource not found");
    error.put("path", request.getRequestURI());
    error.put("method", ex.getHttpMethod().toString());
    return ResponseEntity.status(ex.getStatusCode()).body(error);
  }

  @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
  public ResponseEntity<Map<String, String>> handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException ex,  HttpServletRequest request){
    Map<String, String> error = new HashMap<>();
    error.put("status", ex.getStatusCode().toString());
    error.put("reason", "Method " + ex.getMethod() + " not allowed");
    error.put("path", request.getRequestURI());
    error.put("method", ex.getMethod());
    return ResponseEntity.status(ex.getStatusCode()).body(error);
  }

  @ExceptionHandler(HttpMessageNotReadableException.class)
  public ResponseEntity<Map<String, String>> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex, HttpServletRequest request) {
    Map<String, String> error = new HashMap<>();
    HttpStatus status = HttpStatus.BAD_REQUEST;
    String reason = "Invalid JSON format";

    Throwable cause = ex.getRootCause();
    if (cause instanceof UnrecognizedPropertyException unrecognizedPropertyException) {
      String fieldName = unrecognizedPropertyException.getPropertyName();
      reason = "Field '" + fieldName + "' is not allowed.";
    }

    error.put("status", String.valueOf(status.value()));
    error.put("reason", reason);
    error.put("path", request.getRequestURI());
    return ResponseEntity.status(status).body(error);
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<Map<String, String>> handleGenericException(Exception ex, HttpServletRequest request) {
    Map<String, String> error = new HashMap<>();
    HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;

    error.put("status", String.valueOf(status.value()));
    error.put("reason", ex.getMessage());
    error.put("path", request.getRequestURI());
    return ResponseEntity.status(status).body(error);
  }
}
