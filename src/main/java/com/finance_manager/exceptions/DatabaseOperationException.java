package com.finance_manager.exceptions;

public class DatabaseOperationException extends Exception {
    public DatabaseOperationException(String message) {
        super(message);
    }

    public DatabaseOperationException(String message, Throwable cause) {
        super(message, cause);
    }

    public static class InsufficientFundsException extends Exception {
        public InsufficientFundsException(String message) {
            super(message);
        }

        public InsufficientFundsException(String message, Throwable cause) {
            super(message, cause);
        }
    }
}

