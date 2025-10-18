package br.com.foodhub.controller.user;

import br.com.foodhub.dto.generic.ApiResponse;
import br.com.foodhub.dto.pagination.PageResponseDto;
import br.com.foodhub.dto.user.OwnerRequestDto;
import br.com.foodhub.dto.user.OwnerResponseDto;
import br.com.foodhub.service.user.OwnerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("v1/owners")
@RequiredArgsConstructor
public class OwnerController {

    private final OwnerService service;

    @GetMapping
    public ResponseEntity<PageResponseDto<OwnerResponseDto>> findAll(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String direction
    ) {
        boolean asc = !"desc".equalsIgnoreCase(direction);

        PageResponseDto<OwnerResponseDto> OwnersPage = service.findAllPaginated(page, size, sortBy, asc);
        return ResponseEntity.ok(OwnersPage);
    }


    @GetMapping("/{id}")
    public ResponseEntity<OwnerResponseDto> findByid(@PathVariable Long id) {
        OwnerResponseDto owner = service.findById(id);
        return ResponseEntity.ok(owner);
    }

    @PostMapping
    public ResponseEntity<OwnerResponseDto> create(@Valid @RequestBody OwnerRequestDto dto) {
        OwnerResponseDto created = service.save(dto);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<OwnerResponseDto> update(
            @PathVariable Long id,
            @RequestBody OwnerRequestDto dto
    ) {
        OwnerResponseDto updated = service.update(id, dto);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.ok(new ApiResponse("Usuário deletado com sucesso!"));
    }
}
