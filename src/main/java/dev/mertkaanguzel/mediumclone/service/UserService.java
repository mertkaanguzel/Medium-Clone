package dev.mertkaanguzel.mediumclone.service;

import dev.mertkaanguzel.mediumclone.dto.CreateUserDto;
import dev.mertkaanguzel.mediumclone.dto.UpdateUserDto;
import dev.mertkaanguzel.mediumclone.dto.UserDto;
import dev.mertkaanguzel.mediumclone.exception.UserAlreadyExistsException;
import dev.mertkaanguzel.mediumclone.exception.UserNotFoundException;
import dev.mertkaanguzel.mediumclone.model.UserAccount;
import dev.mertkaanguzel.mediumclone.repository.UserRepository;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public UserAccount findUserByName(String username) {

        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User not found with given username: " + username));
    }

    public UserDto getUserByName(String username, String token) {
        UserAccount user = this.findUserByName(username);
        return UserDto.fromUserAccount(user, token);
    }

    public UserAccount createUser(CreateUserDto createUserDto) {
        if (userRepository.findByEmail(createUserDto.email()).isPresent()) {
            throw new UserAlreadyExistsException("User already exists with given email: " + createUserDto.email());
        }

        UserAccount user = new UserAccount(
                createUserDto.username(),
                passwordEncoder.encode(createUserDto.password()),
                createUserDto.email(),
                null,
                null);
        return userRepository.saveAndFlush(user);
    }

    public UserAccount updateUser(UpdateUserDto updateUserDto, String username) {
        UserAccount user = this.findUserByName(username);
        /*
        try {
            for (Field field : UpdateUserDto.class.getDeclaredFields()) {
                if (!Modifier.isStatic(field.getModifiers())) {
                    field.setAccessible(true);
                    Object val = field.get(updateUserDto);
                    if (val != null) {
                        field.set(user, val);
                    }
                }
            }
        } catch (IllegalAccessException e) {
            System.out.println(e);
        }
        */
        if (updateUserDto.email() != null) user.setEmail(updateUserDto.email());
        if (updateUserDto.username() != null) user.setUsername(updateUserDto.username());
        if (updateUserDto.password() != null) user.setPassword(passwordEncoder.encode(updateUserDto.password()));
        if (updateUserDto.image() != null) user.setImage(updateUserDto.image());
        if (updateUserDto.bio() != null) user.setBio(updateUserDto.bio());

        return userRepository.save(user);
    }

    public UserAccount addFollower(String followed, String followee) {
        UserAccount user = this.findUserByName(followed);
        user.getFollowers().add(this.findUserByName(followee));
        return userRepository.save(user);
    }

    public UserAccount deleteFollower(String followed, String followee) {
        UserAccount user = this.findUserByName(followed);
        user.getFollowers().remove(this.findUserByName(followee));
        return userRepository.save(user);
    }
}
