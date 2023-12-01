package dev.mertkaanguzel.mediumclone.dto;

import dev.mertkaanguzel.mediumclone.model.UserAccount;

import java.util.Objects;

public record ProfileDto(String username, String bio, String image, Boolean following) {
    public static ProfileDto fromUserAccount(UserAccount user, String username) {
        return new ProfileDto(
                user.getUsername(),
                user.getBio(),
                user.getImage(),
                user.getFollowers().stream().anyMatch(follower -> Objects.equals(follower.getUsername(), username))
        );
    }

    public static ProfileDto fromUserAccount(UserAccount user) {
        return new ProfileDto(
                user.getUsername(),
                user.getBio(),
                user.getImage(),
                false
        );
    }
}
