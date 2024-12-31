package org.example.user.entity;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Data
public class Result<T> {
    private Integer code;
    private String message;
    private T data;

    public Result() {

    }

    private Result(Integer code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    private Result(Integer code, String message) {
        this.code = code;
        this.message = message;
    }


    public static <T> Result<T> success(Integer code, String message, T data) {
        return new Result(code, message, data);
    }

    public static <T> Result<T> success(Integer code, String message) {
        return new Result(code, message);
    }

    public static Result error(Integer code, String message) {
        return new Result(code, message);
    }

    public static <T> Result<T> success(T data) {
        return new Result(200, "success", data);
    }
}
