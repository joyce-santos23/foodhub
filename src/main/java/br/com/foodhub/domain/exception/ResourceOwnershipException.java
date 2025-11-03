package br.com.foodhub.domain.exception;

public class ResourceOwnershipException extends RuntimeException {
    public ResourceOwnershipException(String message) {
        super(message);
    }
}
