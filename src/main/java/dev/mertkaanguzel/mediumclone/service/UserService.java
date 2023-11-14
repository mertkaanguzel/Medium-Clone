package dev.mertkaanguzel.mediumclone.service;

import dev.mertkaanguzel.mediumclone.dto.CreateUserDto;
import dev.mertkaanguzel.mediumclone.model.UserAccount;
import dev.mertkaanguzel.mediumclone.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserRepository userRepository;
    //private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository/*, PasswordEncoder passwordEncoder*/) {
        this.userRepository = userRepository;
        //this.passwordEncoder = passwordEncoder;
    }

    public UserAccount getUserByUserName(String username) {
        return userRepository.getUserByUsername(username);
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
}
