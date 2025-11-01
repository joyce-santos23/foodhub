package br.com.foodhub.controller.user;

import br.com.foodhub.controller.api.user.CustomerApi;
import br.com.foodhub.dto.generic.ApiResponseGen;
import br.com.foodhub.dto.pagination.PageResponseDto;
import br.com.foodhub.dto.user.CustomerRequestDto;
import br.com.foodhub.dto.user.CustomerResponseDto;
import br.com.foodhub.dto.user.CustomerUpdateDto;
import br.com.foodhub.entities.user.User;
import br.com.foodhub.service.user.CustomerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/customers")
@RequiredArgsConstructor
public class CustomerController implements CustomerApi {

    private final CustomerService service;

    @GetMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<PageResponseDto<CustomerResponseDto>> findAll(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String direction
    ) {
        boolean asc = !"desc".equalsIgnoreCase(direction);

        PageResponseDto<CustomerResponseDto> customersPage = service.findAllPaginated(page, size, sortBy, asc);
        return ResponseEntity.ok(customersPage);
    }

    @GetMapping("/search")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<PageResponseDto<CustomerResponseDto>> findByName(
            @RequestParam String name,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String direction
    ) {
        boolean asc = !"desc".equalsIgnoreCase(direction);

        // 🚨 Chama a Service para a busca por nome
        PageResponseDto<CustomerResponseDto> ownersPage = service.findByNamePaginated(
                name, page, size, sortBy, asc
        );
        return ResponseEntity.ok(ownersPage);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<CustomerResponseDto> findByid(@PathVariable Long id) {
        CustomerResponseDto customer = service.findById(id);
        return ResponseEntity.ok(customer);
    }

    @GetMapping("/me")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<CustomerResponseDto> getAuthenticatedCustomer(@AuthenticationPrincipal User user) {
        CustomerResponseDto customer = service.findById(user.getId());
        return ResponseEntity.ok(customer);
    }

    @PostMapping
    public ResponseEntity<CustomerResponseDto> create(@Valid @RequestBody CustomerRequestDto dto) {
        CustomerResponseDto created = service.save(dto);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(created);
    }

    @PutMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<CustomerResponseDto> update(
            @PathVariable Long id,
            @RequestBody CustomerUpdateDto dto,
            @AuthenticationPrincipal User user
    ) {
        CustomerResponseDto updated = service.update(id, dto, user);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponseGen> delete(@PathVariable Long id, @AuthenticationPrincipal User user) {
        service.delete(id, user);
        return ResponseEntity.ok(new ApiResponseGen("Usuário deletado com sucesso!"));
    }
}
