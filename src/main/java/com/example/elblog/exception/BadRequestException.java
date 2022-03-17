package com.example.elblog.exception;

/**
 * @author 朱策
 */
public class BadRequestException extends RuntimeException{
    public BadRequestException(String message) {
        super(message);
    }
}
