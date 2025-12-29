package com.lcwd.hotel.exceptions;

public class ResouceNotFoundException extends RuntimeException {
    public ResouceNotFoundException(String s) {
        super(s);
    }
    public ResouceNotFoundException(){
        super("Resource not found");
    }
}
