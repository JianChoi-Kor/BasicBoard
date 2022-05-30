package com.basic.board.exception;

import com.basic.board.util.Helper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RequiredArgsConstructor
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UsernameNotFoundException.class)
    public void handleUsernameNotFoundException(final UsernameNotFoundException e) {
        log.error(e.getMessage());
        Helper.errorMsg(e.getMessage());
    }

    @ExceptionHandler(DataAccessException.class)
    public void dataAccessExceptionHandler(DataAccessException e) {
        log.error(e.getMessage());
        Helper.errorMsg("DataAccessException");
    }
}
