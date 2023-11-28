package dev.mertkaanguzel.mediumclone.dto;

import dev.mertkaanguzel.mediumclone.model.UserAccount;

import java.util.Objects;

public record ProfileDto(String username, String bio, String image, Boolean following) {
    public static ProfileDto fromUserAccount(UserAccount profile, String username) {
        return new ProfileDto(
                profile.getUsername(),
                profile.getBio(),
                profile.getImage(),
                profile.getFollowers().stream().anyMatch(follower -> Objects.equals(follower.getUsername(), username))
        );
    }
}
