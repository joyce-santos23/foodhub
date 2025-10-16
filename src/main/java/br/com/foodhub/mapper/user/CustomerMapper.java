package br.com.foodhub.mapper.user;

import br.com.foodhub.dto.user.CustomerRequestDto;
import br.com.foodhub.dto.user.CustomerResponseDto;
import br.com.foodhub.entities.user.Customer;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CustomerMapper {
    Customer toEntity(CustomerRequestDto dto);
    CustomerResponseDto toResponse(Customer entity);
    List<CustomerResponseDto> toCustomerResponseList(List<Customer> customers);


}
