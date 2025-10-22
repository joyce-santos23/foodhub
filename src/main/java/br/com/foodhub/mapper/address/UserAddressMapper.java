package br.com.foodhub.mapper.address;

import br.com.foodhub.dto.address.AddressBaseDto;
import br.com.foodhub.dto.address.UserAddressResponseDto;
import br.com.foodhub.entities.address.UserAddress;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserAddressMapper {

    @Mapping(target = "address", expression = "java(toAddressBaseDto(userAddress))")
    UserAddressResponseDto toResponseDto(UserAddress userAddress);

    default AddressBaseDto toAddressBaseDto(UserAddress userAddress) {
        var base = userAddress.getAddress();

        return new AddressBaseDto(
                base.getCep(),
                base.getStreet(),
                base.getNeighborhood(),
                base.getCity(),
                base.getState(),
                base.getCountry()
        );
    }
}
