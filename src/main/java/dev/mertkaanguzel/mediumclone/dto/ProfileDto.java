package dev.mertkaanguzel.mediumclone.dto;

import dev.mertkaanguzel.mediumclone.model.UserAccount;

public record ProfileDto(String username, String bio, String image) {
    public static ProfileDto fromUserAccount(UserAccount user) {
        return new ProfileDto(user.getUsername(), user.getBio(), user.getImage());
    }
}
