package com.dasc.spring6mvc.controller;

import jakarta.validation.ConstraintViolationException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class CustomErrorController {

  @ExceptionHandler
  ResponseEntity handleJPAViolations(TransactionSystemException ex) {
    ResponseEntity.BodyBuilder responseEntity = ResponseEntity.badRequest();

    if (ex.getCause().getCause() instanceof ConstraintViolationException) {
      ConstraintViolationException ve = (ConstraintViolationException) ex.getCause().getCause();

      List errors = ve.getConstraintViolations().stream()
          .map(constraintViolation -> {
            Map<String, String> errorMap = new HashMap<>();
            errorMap.put(constraintViolation.getPropertyPath().toString(),
                constraintViolation.getMessage());
            return errorMap;
          }).toList();
      return responseEntity.body(errors);
    }

    return ResponseEntity.badRequest().build();
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  ResponseEntity handleBindErrors(MethodArgumentNotValidException ex) {

    List errorList = ex.getFieldErrors().stream()
        .map(fieldError -> {
          Map<String, String> errorMap = new HashMap<>();
          errorMap.put(fieldError.getField(), fieldError.getDefaultMessage());
          return errorMap;
        }).toList();
    return ResponseEntity.badRequest().body(errorList);
  }

}
