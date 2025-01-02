package com.uevitondev.deliverybackend.domain.address;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record AddressDTO(

        UUID id,
        @NotNull
        @NotBlank
        String name,
        @NotNull
        @NotBlank
        String phoneNumber,
        @NotNull
        @NotBlank
        String zipCode,
        @NotNull
        @NotBlank
        String street,
        @NotNull
        Integer number,
        @NotNull
        @NotBlank
        String district,
        @NotNull
        @NotBlank
        String city,
        @NotNull
        @NotBlank
        String uf,
        String complement

) {
    public AddressDTO(Address address) {
        this(
                address.getId(),
                address.getName(),
                address.getPhoneNumber(),
                address.getZipCode(),
                address.getStreet(),
                address.getNumber(),
                address.getDistrict(),
                address.getCity(),
                address.getUf(),
                address.getComplement()
        );
    }
}

