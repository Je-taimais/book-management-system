package com.shanzhu.book.exception;

/**
 * 图书库存不足异常
 *
 * @author: ShanZhu
 * @date: 2023-12-31
 */
public class BookNotEnoughException extends RuntimeException {
    public BookNotEnoughException() {
        super();
    }

    public BookNotEnoughException(String message) {
        super(message);
    }
}
