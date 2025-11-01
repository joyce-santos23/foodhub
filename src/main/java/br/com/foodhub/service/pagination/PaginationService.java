package br.com.foodhub.service.pagination;

import br.com.foodhub.dto.pagination.PageResponseDto;
import br.com.foodhub.dto.user.OwnerResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.function.Function;

public class PaginationService {

    /**
     * Método genérico de paginação
     * @param repository JpaRepository da entity
     * @param page página atual (0-based)
     * @param size quantidade de elementos por página
     * @param sortBy campo para ordenação
     * @param asc true = ascendente, false = descendente
     * @param mapper função para converter Entity → DTO
     * @param <E> tipo da entity
     * @param <D> tipo do DTO
     * @return PageResponseDto com conteúdo e metadados
     */

    public static <E, D> PageResponseDto<D> paginate(
            JpaRepository<E, ?> repository,
            int page,
            int size,
            String sortBy,
            boolean asc,
            Function<E, D> mapper
    ) {
        Sort sort = asc ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<E> entityPage = repository.findAll(pageable);

        return createPageResponse(entityPage, sortBy, asc, mapper);
    }

    public static <E, D> PageResponseDto<D> paginateWithSearch(
            Function<Pageable, Page<E>> fetcher,
            int page,
            int size,
            String sortBy,
            boolean asc,
            Function<E, D> mapper
    ) {
        Sort sort = asc ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<E> entityPage = fetcher.apply(pageable);

        return createPageResponse(entityPage, sortBy, asc, mapper);
    }

    private static <E, D> PageResponseDto<D> createPageResponse(
            Page<E> entityPage,
            String sortBy,
            boolean asc,
            Function<E, D> mapper
    ) {
        var content = entityPage.getContent().stream()
                .map(mapper)
                .toList();

        return new PageResponseDto<>(
                content,
                entityPage.getNumber() + 1, // Página 1-based
                entityPage.getSize(),
                entityPage.getTotalElements(),
                entityPage.getTotalPages(),
                sortBy,
                asc
        );
    }

}
