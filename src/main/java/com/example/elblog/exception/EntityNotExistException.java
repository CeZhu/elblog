package com.example.elblog.exception;

/**
 * @author 朱策
 */
public class EntityNotExistException extends RuntimeException{
    public EntityNotExistException(String message) {
        super(message);
    }
}
