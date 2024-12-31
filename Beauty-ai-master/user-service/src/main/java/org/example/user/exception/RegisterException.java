package org.example.user.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.example.user.entity.Result;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Data
@AllArgsConstructor
public class RegisterException extends RuntimeException{

    private String message;
    private Integer code;
}
