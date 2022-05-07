package com.basic.board.advice;

import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedList;

@Component
public class Response {

  @Data
  private static class Body {

    private int state;
    private Object data;
    private String result;
    private String message;
    private Object error;
  }

  public ResponseEntity<?> success(Object data, String msg, HttpStatus status) {
    Body body = new Body();
    body.setState(status.value());
    body.setData(data);
    body.setResult("success");
    body.setMessage(msg);
    body.setError(Collections.emptyList());
    return ResponseEntity.ok(body);
  }

  public ResponseEntity<?> success(String msg) {
    return success(Collections.emptyList(), msg, HttpStatus.OK);
  }

  public ResponseEntity<?> success(Object data) {
    return success(data, null, HttpStatus.OK);
  }

  public ResponseEntity<?> successForStringData(String data) {
    return success(data, null, HttpStatus.OK);
  }

  public ResponseEntity<?> success() {
    return success(Collections.emptyList(), null, HttpStatus.OK);
  }

  public ResponseEntity<?> fail(Object data, String msg, HttpStatus status) {
    Body body = new Body();
    body.setState(status.value());
    body.setResult("fail");
    body.setMessage(msg);
    body.setData(data);
    body.setError(Collections.emptyList());
    return ResponseEntity.ok().body(body);
  }

  public ResponseEntity<?> fail(String msg, HttpStatus status) {
    return fail(Collections.emptyList(), msg, status);
  }

  public ResponseEntity<?> fail(Object data, String msg) {
    return fail(data, msg, HttpStatus.BAD_REQUEST);
  }

  public ResponseEntity<?> fail(String msg) {
    return fail(Collections.emptyList(), msg, HttpStatus.BAD_REQUEST);
  }

  public ResponseEntity<?> fail(LinkedList<LinkedHashMap<String, String>> error) {
    Body body = new Body();
    body.setState(HttpStatus.BAD_REQUEST.value());
    body.setData(Collections.emptyList());
    body.setResult("false");
    body.setMessage("");
    body.setError(error);
    return ResponseEntity.ok().body(body);
  }

  public ResponseEntity<?> validResponse(Errors errors) {

    LinkedList<LinkedHashMap<String, String>> responseErrors = new LinkedList<>();
    for (FieldError f : errors.getFieldErrors()) {
      LinkedHashMap<String, String> errorMessage = new LinkedHashMap<>();
      errorMessage.put("field", f.getField());
      errorMessage.put("message", f.getDefaultMessage());
      responseErrors.add(errorMessage);
    }

    return this.fail(responseErrors);
  }

  public ResponseEntity<?> validResponse(Errors errors, LinkedList<LinkedHashMap<String, String>> addError) {

    LinkedList<LinkedHashMap<String, String>> responseErrors = new LinkedList<>();
    for (FieldError f : errors.getFieldErrors()) {
      LinkedHashMap<String, String> errorMessage = new LinkedHashMap<>();
      errorMessage.put("field", f.getField());
      errorMessage.put("message", f.getDefaultMessage());
      responseErrors.add(errorMessage);
    }

    responseErrors.addAll(addError);

    return this.fail(responseErrors);
  }
}
