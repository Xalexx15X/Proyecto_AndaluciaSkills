package com.andaluciaskills.andaluciasckills.Error;

public class FileNotFoundException extends Exception {
    private static final long serialVersionUID = 1L;
    
    public FileNotFoundException(String message) {
        super(message);
    }   
    
    public FileNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

}
