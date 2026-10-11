package com.anurag.parking.exception;
import org.springframework.http.*; import org.springframework.web.bind.annotation.*;
@RestControllerAdvice
public class GlobalExceptionHandler {
 @ExceptionHandler(IllegalArgumentException.class)
 public ResponseEntity<String> handle(IllegalArgumentException e){return ResponseEntity.badRequest().body(e.getMessage());}
}
