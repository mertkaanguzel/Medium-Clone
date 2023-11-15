package dev.mertkaanguzel.mediumclone.controller;

import dev.mertkaanguzel.mediumclone.dto.CreateArticleDto;
import dev.mertkaanguzel.mediumclone.dto.LoginDto;
import dev.mertkaanguzel.mediumclone.dto.UpdateUserDto;
import dev.mertkaanguzel.mediumclone.dto.UserDto;
import dev.mertkaanguzel.mediumclone.model.Article;
import dev.mertkaanguzel.mediumclone.model.UserAccount;
import dev.mertkaanguzel.mediumclone.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api/user")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<UserDto> getUser(Principal principal) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        JwtAuthenticationToken jwtAuthenticationToken = (JwtAuthenticationToken) authentication;
        String token = jwtAuthenticationToken.getToken().getTokenValue();

        return ResponseEntity.ok(UserDto.fromUserAccount(userService.getUserByUserName(principal.getName()), token));

    }

    //??? @PreAuthorize("#entity.username == authentication.name") //?????
    @PutMapping
    public ResponseEntity<UserDto> updateUser(@Valid @RequestBody UpdateUserDto updateUserDto, Principal principal) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        JwtAuthenticationToken jwtAuthenticationToken = (JwtAuthenticationToken) authentication;
        String token = jwtAuthenticationToken.getToken().getTokenValue();

        return ResponseEntity.ok(UserDto.fromUserAccount(userService.updateUser(updateUserDto, principal.getName()), token));

    }
}


