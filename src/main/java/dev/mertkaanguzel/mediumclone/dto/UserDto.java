package dev.mertkaanguzel.mediumclone.dto;

import dev.mertkaanguzel.mediumclone.model.UserAccount;

public record UserDto(String email, String token, String username, String bio, String image) {
    public static UserDto fromUserAccount(UserAccount user, String token) {
        return new UserDto(user.getEmail(), token, user.getUsername(), user.getBio(), user.getImage());
    }
}
