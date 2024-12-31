package org.example.user.exception;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UpdateException extends RuntimeException{
  private Integer code;
  private String message;
}
