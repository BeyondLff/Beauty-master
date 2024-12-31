package org.example.user.handler;

import org.example.user.entity.Result;
import org.example.user.exception.*;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(LoginException.class)
    public Result LoginFail(LoginException e){
        return Result.error(e.getCode(),e.getMessage());
    }

    @ExceptionHandler(RegisterException.class)
    public Result RegisterFail(RegisterException e){return Result.error(e.getCode(),e.getMessage());}

    @ExceptionHandler(ActionException.class)
    public Result DeleteFail(ActionException e){return Result.error(e.getCode(),e.getMessage());}

    @ExceptionHandler(ArgException.class)
    public Result ArgError(ActionException e){return Result.error(e.getCode(),e.getMessage());}

    @ExceptionHandler(AddException.class)
    public Result AddFile(AddException e){return Result.error(e.getCode(),e.getMessage());}

    @ExceptionHandler(UploadException.class)
    public Result uploadImage(UploadException e){return Result.error(e.getCode(),e.getMessage());}
}
