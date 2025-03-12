package com.uevitondev.deliverybackend.domain.store;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record NewStoreDTO(

        @NotBlank
        @Size(min = 1, max = 30)
        String name,

        @NotBlank
        @Size(min = 1, max = 16)
        String phoneNumber,

        @NotBlank
        @Size(min = 1, max = 16)
        String type
) {

}