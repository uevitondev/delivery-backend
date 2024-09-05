package com.uevitondev.deliverybackend.domain.address;

import java.util.UUID;

public record NewAddressDTO(
        UUID id,
        String name,
        String phoneNumber,
        String street,
        Integer number,
        String district,
        String city,
        String uf,
        String complement,
        String zipCode
) {
    public NewAddressDTO(Address address) {
        this(
                address.getId(),
                address.getName(),
                address.getPhoneNumber(),
                address.getStreet(),
                address.getNumber(),
                address.getDistrict(),
                address.getCity(),
                address.getUf(),
                address.getComplement(),
                address.getZipCode()
        );
    }
}

