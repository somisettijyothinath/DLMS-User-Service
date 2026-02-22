package com.example.library.user.exception;

public class AppException extends RuntimeException {
	private static final long serialVersionUID = 1L;
    private final int statusCode;
    
    public AppException(int statusCode, String message) {
        super(message);
        this.statusCode = statusCode;
    }
    
    public int getStatusCode() {
        return statusCode;
    }
}
