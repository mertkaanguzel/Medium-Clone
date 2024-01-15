package dev.mertkaanguzel.mediumclone.controller;

import dev.mertkaanguzel.mediumclone.dto.ProfileDto;
import dev.mertkaanguzel.mediumclone.dto.UserDto;
import dev.mertkaanguzel.mediumclone.service.UserService;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api/profiles")
@SecurityRequirement(name = "mediumapi")
public class ProfileController {
    private final UserService userService;

    public ProfileController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{username}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "404", description = "Not Found"),
    })
    public ResponseEntity<ProfileDto> getProfile(@PathVariable String username, Principal principal) {
        return ResponseEntity.ok(ProfileDto.fromUserAccount(userService.findUserByName(username), principal.getName()));
    }

    @PostMapping("/{username}/follow")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "404", description = "Not Found"),
    })
    public ResponseEntity<ProfileDto> followProfile(@PathVariable String username, Principal principal) {
        return ResponseEntity.ok(ProfileDto.fromUserAccount(userService.addFollower(username, principal.getName()), principal.getName()));
    }

    @DeleteMapping("/{username}/follow")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "404", description = "Not Found"),
    })
    public ResponseEntity<ProfileDto> unfollowProfile(@PathVariable String username, Principal principal) {
        return ResponseEntity.ok(ProfileDto.fromUserAccount(userService.deleteFollower(username, principal.getName()), principal.getName()));
    }
}
