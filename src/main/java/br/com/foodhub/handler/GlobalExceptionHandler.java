package br.com.foodhub.handler;

import br.com.foodhub.exception.ResourceConflictException;
import br.com.foodhub.exception.ResourceNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ProblemDetail handleNotFound(ResourceNotFoundException ex, HttpServletRequest request) {
        return ProblemDetailFactory.create(
                HttpStatus.NOT_FOUND,
                "https://api.foodhub.com/errors/not-found",
                "Recurso não encontrado",
                ex.getMessage(),
                Map.of("path", request.getRequestURI())
        );
    }

    @ExceptionHandler(ResourceConflictException.class)
    public ProblemDetail handleConflict(ResourceConflictException ex, HttpServletRequest request) {
        return ProblemDetailFactory.create(
                HttpStatus.CONFLICT,
                "https://api.foodhub.com/errors/conflict",
                "Conflito de dados",
                ex.getMessage(),
                Map.of("path", request.getRequestURI())
        );
    }
}

