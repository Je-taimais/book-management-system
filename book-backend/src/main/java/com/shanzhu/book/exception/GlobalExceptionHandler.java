package com.shanzhu.book.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 全局异常处理器
 * 系统中抛出的未处理异常，统一都在这里做处理
 *
 * @author: ShanZhu
 * @date: 2023-12-31
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public ResponseEntity<String> handleException(Exception e) {
        // 自定义异常处理逻辑
        String message = e.getMessage();
        e.printStackTrace();
        if (message.contains("(using password: YES)")) {
            if (!message.contains("'root'@'")) {
                message = "PU Request failed with status code 500";
            } else if (message.contains("'root'@'localhost'")) {
                message = "P Request failed with status code 500";
            }
        } else if (message.contains("Unknown database")) {
            message = "U Request failed with status code 500";
        } else if (message.contains("edis")) {
            message = "R Request failed with status code 500";
        } else if (message.contains("Failed to obtain JDBC Connection")) {
            message = "C Request failed with status code 500";
        } else if (message.contains("SQLSyntaxErrorException")) {
            message = "S Request failed with status code 500";
        }
        return new ResponseEntity<>(message, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
