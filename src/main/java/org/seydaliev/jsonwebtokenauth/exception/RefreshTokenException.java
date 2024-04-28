package org.seydaliev.jsonwebtokenauth.exception;

public class RefreshTokenException extends  RuntimeException{
    public RefreshTokenException(String string) {
        super(string);
    }

    public RefreshTokenException() {
    }
}
