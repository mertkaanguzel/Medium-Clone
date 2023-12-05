package dev.mertkaanguzel.mediumclone.controller;

import dev.mertkaanguzel.mediumclone.dto.CreateUserDto;
import dev.mertkaanguzel.mediumclone.dto.LoginDto;
import dev.mertkaanguzel.mediumclone.dto.UserDto;
import dev.mertkaanguzel.mediumclone.model.UserAccount;
import dev.mertkaanguzel.mediumclone.service.AuthService;
import dev.mertkaanguzel.mediumclone.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
public class AuthController {
    private final UserService userService;
    private final AuthService authService;
    private final AuthenticationManager authenticationManager;

    public AuthController(UserService userService, AuthService authService, AuthenticationManager authenticationManager) {
        this.userService = userService;
        this.authService = authService;
        this.authenticationManager = authenticationManager;
    }

    @PostMapping
    public ResponseEntity<UserDto> register(@Valid @RequestBody CreateUserDto createUserDto) {
        UserAccount user = userService.createUser(createUserDto);
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(createUserDto.email(), createUserDto.password()));

        return ResponseEntity.ok(UserDto.fromUserAccount(user, authService.generateToken(authentication)));
    }

    @PostMapping("/login")
    public ResponseEntity<UserDto> login(@Valid @RequestBody LoginDto loginDto) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginDto.email(), loginDto.password()));

        return ResponseEntity.ok(userService.getUserByName(authentication.getName(), authService.generateToken(authentication)));
    }
}
