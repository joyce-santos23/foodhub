package br.com.foodhub.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.Map;

public class ProblemDetailFactory {

    /**
     * Cria um ProblemDetail genérico
     * @param status Status HTTP
     * @param type URI que identifica o tipo do problema
     * @param title Título resumido
     * @param detail Mensagem detalhada (dinâmica)
     * @param extraProperties Map de propriedades extras (opcional)
     * @return ProblemDetail configurado
     */
    public static ProblemDetail create(
            HttpStatus status,
            String type,
            String title,
            String detail,
            Map<String, Object> extraProperties
    ) {
        ProblemDetail problem = ProblemDetail.forStatus(status);
        problem.setType(URI.create(type));
        problem.setTitle(title);
        problem.setDetail(detail);
        problem.setProperty("timestamp", LocalDateTime.now());

        if (extraProperties != null) {
            extraProperties.forEach(problem::setProperty);
        }

        return problem;
    }

    // Sobrecarga sem extraProperties
    public static ProblemDetail create(
            HttpStatus status,
            String type,
            String title,
            String detail
    ) {
        return create(status, type, title, detail, null);
    }
}
