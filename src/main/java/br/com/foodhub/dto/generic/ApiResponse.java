package br.com.foodhub.dto.generic;

public record ApiResponse<T>(
        boolean success,
        String message,
        T data
) {

    public ApiResponse(String message) {
        this(false, message, null);
    }
    public ApiResponse(boolean success, String message) {
        this(success, message, null);
    }
}