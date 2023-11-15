package dev.mertkaanguzel.mediumclone.service;

import dev.mertkaanguzel.mediumclone.dto.CreateUserDto;
import dev.mertkaanguzel.mediumclone.dto.UpdateUserDto;
import dev.mertkaanguzel.mediumclone.dto.UserDto;
import dev.mertkaanguzel.mediumclone.model.UserAccount;
import dev.mertkaanguzel.mediumclone.repository.UserRepository;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;
    //private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository/*, PasswordEncoder passwordEncoder*/) {
        this.userRepository = userRepository;
        //this.passwordEncoder = passwordEncoder;
    }

    public UserAccount getUserByUserName(String username) {

        return userRepository.getUserByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with given username: " + username));
    }

    public UserDto findByUserName(String username, String token) {
        UserAccount user = this.getUserByUserName(username);
        return UserDto.fromUserAccount(user, token);
    }

    public UserAccount createUser(CreateUserDto createUserDto) {
        UserAccount user = new UserAccount(
                createUserDto.username(),
                createUserDto.password(),//passwordEncoder.encode(createUserDto.password()),
                createUserDto.email(),
                null,
                null);
        return userRepository.saveAndFlush(user);
    }

    public UserAccount updateUser(UpdateUserDto updateUserDto, String username) {
        UserAccount user = this.getUserByUserName(username);
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
        if (updateUserDto.password() != null) user.setPassword(updateUserDto.password());
        if (updateUserDto.image() != null) user.setImage(updateUserDto.image());
        if (updateUserDto.bio() != null) user.setBio(updateUserDto.bio());

        return userRepository.save(user);
    }
}
