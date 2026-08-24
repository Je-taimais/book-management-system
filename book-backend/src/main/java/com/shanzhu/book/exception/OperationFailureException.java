package com.shanzhu.book.exception;

/**
 * 系统操作视频异常
 *
 * @author: ShanZhu
 * @date: 2023-12-31
 */
public class OperationFailureException extends RuntimeException {
    public OperationFailureException() {
    }

    public OperationFailureException(String message) {
        super(message);
    }
}
