package br.com.foodhub.domain.mapper.user;

import br.com.foodhub.application.dto.user.CustomerRequestDto;
import br.com.foodhub.application.dto.user.CustomerResponseDto;
import br.com.foodhub.domain.entities.user.Customer;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CustomerMapper {
    Customer toEntity(CustomerRequestDto dto);
    CustomerResponseDto toResponse(Customer entity);



}
