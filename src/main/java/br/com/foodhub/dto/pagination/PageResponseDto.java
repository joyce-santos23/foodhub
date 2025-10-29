package br.com.foodhub.dto.pagination;

import java.util.List;

public record PageResponseDto<T>(
        List<T> content,
        int currentPage,
        int pageSize,
        long totalElements,
        int totalPages,
        String sortBy,
        boolean asc
) {
}
