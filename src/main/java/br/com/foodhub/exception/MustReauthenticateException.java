package br.com.foodhub.exception;

public class MustReauthenticateException extends RuntimeException {
    public MustReauthenticateException(String message) {
        super(message);
    }
}
