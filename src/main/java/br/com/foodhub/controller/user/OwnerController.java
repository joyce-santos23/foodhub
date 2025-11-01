package br.com.foodhub.controller.user;

import br.com.foodhub.controller.api.user.OwnerApi;
import br.com.foodhub.dto.generic.ApiResponseGen;
import br.com.foodhub.dto.pagination.PageResponseDto;
import br.com.foodhub.dto.user.OwnerRequestDto;
import br.com.foodhub.dto.user.OwnerResponseDto;
import br.com.foodhub.dto.user.OwnerUpdateDto;
import br.com.foodhub.entities.user.User;
import br.com.foodhub.service.user.OwnerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/owners")
@RequiredArgsConstructor
public class OwnerController implements OwnerApi {

    private final OwnerService service;

    @GetMapping
    @PreAuthorize("hasAuthority('ADMIN')")
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

    @GetMapping("/search")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<PageResponseDto<OwnerResponseDto>> findByName(
            @RequestParam String name,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String direction
    ) {
        boolean asc = !"desc".equalsIgnoreCase(direction);

        PageResponseDto<OwnerResponseDto> ownersPage = service.findByNamePaginated(
                name, page, size, sortBy, asc
        );
        return ResponseEntity.ok(ownersPage);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<OwnerResponseDto> findByid(@PathVariable Long id) {
        OwnerResponseDto owner = service.findById(id);
        return ResponseEntity.ok(owner);
    }

    @GetMapping("/me")
    @PreAuthorize("isAuthenticated()")
    public  ResponseEntity<OwnerResponseDto> getAuthenticatedOwner(@AuthenticationPrincipal User user) {
        OwnerResponseDto owner = service.findById(user.getId());
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
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<OwnerResponseDto> update(
            @PathVariable Long id,
            @RequestBody OwnerUpdateDto dto,
            @AuthenticationPrincipal User user
    ) {
        OwnerResponseDto updated = service.update(id, dto, user);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponseGen> delete(@PathVariable Long id, @AuthenticationPrincipal User user) {
        service.delete(id, user);
        return ResponseEntity.ok(new ApiResponseGen("Usuário deletado com sucesso!"));
    }
}
