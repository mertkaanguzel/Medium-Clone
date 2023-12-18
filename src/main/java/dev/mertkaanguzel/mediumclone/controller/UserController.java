package dev.mertkaanguzel.mediumclone.controller;

import dev.mertkaanguzel.mediumclone.dto.UpdateUserDto;
import dev.mertkaanguzel.mediumclone.dto.UserDto;
import dev.mertkaanguzel.mediumclone.service.AuthService;
import dev.mertkaanguzel.mediumclone.service.UserService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/user")
@SecurityRequirement(name = "mediumapi")
public class UserController {
    private final UserService userService;
    private final AuthService authService;

    public UserController(UserService userService, AuthService authService, Clock clock) {
        this.userService = userService;
        this.authService = authService;
    }

    @GetMapping
    public ResponseEntity<UserDto> getUser(Principal principal) {
        return ResponseEntity.ok(UserDto.fromUserAccount(userService.findUserByName(principal.getName()),
                authService.getToken()));
    }

    //??? @PreAuthorize("#entity.username == authentication.name") //?????
    @PutMapping
    public ResponseEntity<UserDto> updateUser(@Valid @RequestBody UpdateUserDto updateUserDto, Principal principal) {
        return ResponseEntity.ok(UserDto.fromUserAccount(userService.updateUser(updateUserDto, principal.getName()),
                authService.getToken()));
    }
}


