package com.example.elblog.exception.handler;

import com.example.elblog.exception.BadRequestException;
import com.example.elblog.exception.EntityNotExistException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author 朱策
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ResponseEntity<Object> methodArgumentNotValidExceptionHandler(MethodArgumentNotValidException e) {
        List<ObjectError> allErrors = e.getBindingResult().getAllErrors();
        String message = allErrors.stream().map(s -> s.getDefaultMessage()).collect(Collectors.joining(";"));
        return new ResponseEntity<>(ApiError.error(message), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = UsernameNotFoundException.class)
    public ResponseEntity<Object> usernameNotFoundExceptionHandler(UsernameNotFoundException e) {
        return new ResponseEntity<>(ApiError.error(e.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = BadCredentialsException.class)
    public ResponseEntity<Object> badCredentialsExceptionHandler(BadCredentialsException e) {
        return new ResponseEntity<>(ApiError.error(e.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = EntityNotExistException.class)
    public ResponseEntity<Object> entityNotExistExceptionHandler(EntityNotExistException e) {
        return new ResponseEntity<>(ApiError.error(e.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = BadRequestException.class)
    public ResponseEntity<Object> badRequestExceptionHandler(BadRequestException e) {
        return new ResponseEntity<>(ApiError.error(e.getMessage()),HttpStatus.BAD_REQUEST);
    }
}
