package br.com.foodhub.domain.exception;

public class MustReauthenticateException extends RuntimeException {
    public MustReauthenticateException(String message) {
        super(message);
    }
}
