package dev.mertkaanguzel.mediumclone.controller;

import dev.mertkaanguzel.mediumclone.dto.ProfileDto;
import dev.mertkaanguzel.mediumclone.dto.UserDto;
import dev.mertkaanguzel.mediumclone.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api/profiles")
public class ProfileController {
    private final UserService userService;

    public ProfileController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{username}")
    public ResponseEntity<ProfileDto> getProfile(@PathVariable String username, Principal principal) {
        return ResponseEntity.ok(ProfileDto.fromUserAccount(userService.getUserByUserName(username), principal.getName()));
    }

    @PostMapping("/{username}/follow")
    public ResponseEntity<ProfileDto> followProfile(@PathVariable String username, Principal principal) {
        return ResponseEntity.ok(ProfileDto.fromUserAccount(userService.addFollower(username, principal.getName()), principal.getName()));
    }

    @DeleteMapping("/{username}/follow")
    public ResponseEntity<ProfileDto> unfollowProfile(@PathVariable String username, Principal principal) {
        return ResponseEntity.ok(ProfileDto.fromUserAccount(userService.deleteFollower(username, principal.getName()), principal.getName()));
    }
}
