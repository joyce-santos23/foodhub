package br.com.foodhub.domain.mapper.user;

import br.com.foodhub.application.dto.user.OwnerRequestDto;
import br.com.foodhub.application.dto.user.OwnerResponseDto;
import br.com.foodhub.domain.entities.user.Owner;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface OwnerMapper {
    Owner toEntity(OwnerRequestDto dto);
    OwnerResponseDto toResponse(Owner entity);

}
