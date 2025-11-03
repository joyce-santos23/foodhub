package br.com.foodhub.infrastructure.handler;

import br.com.foodhub.application.dto.generic.ApiResponseGen;
import br.com.foodhub.domain.exception.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;
import java.util.stream.Collectors;

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

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail handleValidationErrors(MethodArgumentNotValidException ex, HttpServletRequest request) {
        // Captura os erros de campo
        Map<String, String> fieldErrors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .collect(Collectors.toMap(
                        fieldError -> fieldError.getField(),
                        fieldError -> fieldError.getDefaultMessage(),
                        (existing, replacement) -> existing // evita duplicatas
                ));

        return ProblemDetailFactory.create(
                HttpStatus.BAD_REQUEST,
                "https://api.foodhub.com/errors/validation",
                "Erro de validação",
                "Existem campos inválidos na requisição",
                Map.of(
                        "path", request.getRequestURI(),
                        "fields", fieldErrors
                )
        );
    }

    @ExceptionHandler(ResourceOwnershipException.class)
    public ProblemDetail handleOwnershipError(ResourceOwnershipException ex, HttpServletRequest request) {
        return ProblemDetailFactory.create(
                HttpStatus.FORBIDDEN,
                "https://api.foodhub.com/errors/ownership-violation",
                "Violação de Posse",
                ex.getMessage(),
                Map.of("path", request.getRequestURI())
        );
    }


    @ExceptionHandler(AuthenticationException.class)
    public ProblemDetail handleAuthenticationError(AuthenticationException ex, HttpServletRequest request) {
        return ProblemDetailFactory.create(
                HttpStatus.UNAUTHORIZED, // 401
                "https://api.foodhub.com/errors/authentication-failed",
                "Falha na autenticação",
                ex.getMessage(),
                Map.of("path", request.getRequestURI())
        );
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ProblemDetail handleAccessDenied(AccessDeniedException ex, HttpServletRequest request) {
        return ProblemDetailFactory.create(
                HttpStatus.FORBIDDEN, // 403
                "https://api.foodhub.com/errors/access-denied",
                "Acesso negado",
                "Você não tem permissão (Role) para acessar este recurso.",
                Map.of("path", request.getRequestURI())
        );
    }


    @ExceptionHandler(MustReauthenticateException.class)
    public ResponseEntity<ApiResponseGen<?>> handleReauthenticationRequired(MustReauthenticateException ex, HttpServletRequest request) {

        return ResponseEntity
                .status(HttpStatus.ACCEPTED)
                .body(new ApiResponseGen<>(
                        true,
                        ex.getMessage(),
                        null
                ));
    }
}

