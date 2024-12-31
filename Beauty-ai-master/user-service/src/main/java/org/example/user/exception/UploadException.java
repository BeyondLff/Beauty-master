package org.example.user.exception;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UploadException extends RuntimeException{
    private Integer code;
    private String message;
}