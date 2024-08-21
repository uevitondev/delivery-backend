package com.uevitondev.deliverybackend.domain.user;

public record UserProfileDTO(
        String fullName,
        String phoneNumber,
        String username
) {
    public UserProfileDTO(User user) {
        this(user.getFirstName() + " " + user.getLastName() , user.getPhoneNumber(), user.getUsername());
    }
}
