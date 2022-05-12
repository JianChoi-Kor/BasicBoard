package com.basic.board.exception;

import com.basic.board.advice.Response;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RequiredArgsConstructor
@RestControllerAdvice
public class GlobalExceptionHandler {

    private final Response response;

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<?> handleUsernameNotFoundException(final UsernameNotFoundException e) {
        log.error(e.getMessage());
        return response.fail(e.getMessage());
    }

    @ExceptionHandler(DataAccessException.class)
    public ResponseEntity<?> dataAccessExceptionHandler(DataAccessException e) {
        log.error(e.getMessage());
        return response.fail("DataAccessException");
    }
}
