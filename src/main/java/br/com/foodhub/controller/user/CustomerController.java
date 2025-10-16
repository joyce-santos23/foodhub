package br.com.foodhub.controller.user;

import br.com.foodhub.dto.pagination.PageResponseDto;
import br.com.foodhub.dto.user.CustomerRequestDto;
import br.com.foodhub.dto.user.CustomerResponseDto;
import br.com.foodhub.service.user.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("v1/customers")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService service;

    @GetMapping
    public ResponseEntity<PageResponseDto<CustomerResponseDto>> findAll(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") Sort.Direction direction
    ) {
        boolean asc = direction == Sort.Direction.ASC;

        PageResponseDto<CustomerResponseDto> customersPage = service.findAllPaginated(page, size, sortBy, asc);
        return ResponseEntity.ok(customersPage);
    }


    @GetMapping("/{id}")
    public ResponseEntity<CustomerResponseDto> findByid(@PathVariable Long id) {
        CustomerResponseDto customer = service.findById(id);
        return ResponseEntity.ok(customer);
    }

    @PostMapping
    public ResponseEntity<CustomerResponseDto> create(@RequestBody CustomerRequestDto dto) {
        CustomerResponseDto created = service.save(dto);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CustomerResponseDto> update(
            @PathVariable Long id,
            @RequestBody CustomerRequestDto dto
    ) {
        CustomerResponseDto updated = service.update(id, dto);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity
                .noContent()
                .build();
    }
}
