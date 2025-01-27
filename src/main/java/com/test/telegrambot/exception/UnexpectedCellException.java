package com.test.telegrambot.exception;

/**
 * Исключение, возникающее при попытке обработать неожиданную или занятую ячейку
 */
public class UnexpectedCellException extends RuntimeException{

    public UnexpectedCellException(String message) {
        super(message);
    }

}
