package com.alten.booking.exceptions;


import com.alten.booking.exceptions.entity.ExceptionResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

  private ExceptionResponse buildExceptionResponse(final Exception exception) {
    final ExceptionResponse response = new ExceptionResponse();
    response.setDateTime(LocalDateTime.now());
    response.setMessage(exception.getMessage());
    return response;
  }

  @ExceptionHandler(IllegalArgumentException.class)
  public ResponseEntity<Object> handleExceptions(final IllegalArgumentException exception) {
    return new ResponseEntity<>(buildExceptionResponse(exception), HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(ResourceNotFoundException.class)
  public ResponseEntity<Object> handleExceptions(final ResourceNotFoundException exception) {
    return new ResponseEntity<>(buildExceptionResponse(exception), HttpStatus.NOT_FOUND);
  }
  
  @ExceptionHandler(NotAvailableException.class)
  public ResponseEntity<Object> handleExceptions(final NotAvailableException exception) {
    return new ResponseEntity<>(buildExceptionResponse(exception), HttpStatus.CONFLICT);
  }
}
