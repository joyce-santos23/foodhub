package br.com.foodhub.mapper.user;

import br.com.foodhub.dto.user.OwnerRequestDto;
import br.com.foodhub.dto.user.OwnerResponseDto;
import br.com.foodhub.entities.user.Owner;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface OwnerMapper {
    Owner toEntity(OwnerRequestDto dto);
    OwnerResponseDto toResponse(Owner entity);
    List<OwnerResponseDto> toOwnerResponseList(List<Owner> owners);
}
