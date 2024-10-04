package com.uevitondev.deliverybackend.domain.user;

public record UserProfileDTO(
        String firstName,
        String lastName,
        String email,
        String phoneNumber

) {
    public UserProfileDTO(User user) {
        this(
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getPhoneNumber()
        );
    }
}
