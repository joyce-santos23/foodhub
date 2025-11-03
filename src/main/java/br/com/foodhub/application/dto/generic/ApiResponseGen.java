package br.com.foodhub.application.dto.generic;

public record ApiResponseGen<T>(
        boolean success,
        String message,
        T data
) {

    public ApiResponseGen(String message) {
        this(false, message, null);
    }
    public ApiResponseGen(boolean success, String message) {
        this(success, message, null);
    }
}