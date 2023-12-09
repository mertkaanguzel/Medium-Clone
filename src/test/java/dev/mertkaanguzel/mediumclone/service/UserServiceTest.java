package dev.mertkaanguzel.mediumclone.service;

import dev.mertkaanguzel.mediumclone.dto.*;
import dev.mertkaanguzel.mediumclone.exception.ArticleAlreadyExistsException;
import dev.mertkaanguzel.mediumclone.exception.ArticleNotFoundException;
import dev.mertkaanguzel.mediumclone.exception.UserAlreadyExistsException;
import dev.mertkaanguzel.mediumclone.exception.UserNotFoundException;
import dev.mertkaanguzel.mediumclone.model.Article;
import dev.mertkaanguzel.mediumclone.model.UserAccount;
import dev.mertkaanguzel.mediumclone.repository.ArticleRepository;
import dev.mertkaanguzel.mediumclone.repository.UserRepository;
import org.junit.jupiter.api.*;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.Clock;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static dev.mertkaanguzel.mediumclone.TestSupport.*;
import static dev.mertkaanguzel.mediumclone.TestSupport.getLocalDateTime;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.AdditionalAnswers.*;
import static org.mockito.Mockito.*;


public class UserServiceTest {
    private UserService userService;
    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;

    private final String username = "Jacob";
    private final UserAccount user = new UserAccount("Jacob", "", "jake@jake.jake", null, null);
    private final ProfileDto profileDto = new ProfileDto("Jacob", "I work at statefarm","https://api.realworld.io/images/smiley-cyrus.jpg", false);
    private final UserDto userDto = new UserDto("jake@jake.jake", "", "Jacob", null, null);

    @BeforeEach
    void setUp() {
        userRepository = mock(UserRepository.class);
        passwordEncoder = mock(PasswordEncoder.class);

        userService = new UserService(userRepository, passwordEncoder);
    }

    @Test
    void testFindUserByName_whenUserNameNotExists_shouldThrowUserNotFoundException() {
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.empty());
        assertThrows(UserNotFoundException.class, () -> userService.findUserByName(username));

    }

    @Test
    void testFindUserByName_whenUserNameExists_shouldReturnUser() {
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(user));
        UserAccount result = userService.findUserByName(username);
        assertEquals(user, result);
    }

    @Test
    void testGetUserByName_whenUserNameNotExists_shouldThrowUserNotFoundException() {
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.empty());
        assertThrows(UserNotFoundException.class, () -> userService.getUserByName(username, userDto.token()));
    }

    @Test
    void testGetUserByName_whenUserNameExists_shouldReturnUserDto() {
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(user));
        UserDto result = userService.getUserByName(username, userDto.token());
        assertEquals(userDto, result);
    }

    @Test
    void testCreateUser_whenUserEmailExists_shouldThrowUserAlreadyExistsException() {
        CreateUserDto createUserDto = new CreateUserDto("Jacob", "jake@jake.jake", "jakejake");

        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(user));
        when(userRepository.saveAndFlush(any(UserAccount.class))).thenAnswer(i -> i.getArguments()[0]);

        assertThrows(UserAlreadyExistsException.class, () -> userService.createUser(createUserDto));
    }

    @Test
    void testCreateUser_whenUserEmailNotExist_shouldCreateUser() {
        CreateUserDto createUserDto = new CreateUserDto("Jacob", "jake@jake.jake", "jakejake");

        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(anyString())).thenReturn("");
        when(userRepository.saveAndFlush(any(UserAccount.class))).thenAnswer(i -> i.getArguments()[0]);

        UserAccount result = userService.createUser(createUserDto);
        //assertEquals(user, result);
        assertEquals(user.getUsername(), result.getUsername());
        assertEquals(user.getPassword(), result.getPassword());
        assertEquals(user.getEmail(), result.getEmail());
        assertEquals(user.getBio(), result.getBio());
        assertEquals(user.getImage(), result.getImage());
    }

    @Test
    void testupdateUser_shouldReturnUser() {
        UserAccount updatedUser = user;
        updatedUser.setImage("I work at statefarmm");
        updatedUser.setBio("https://api.realworld.io/images/smiley-cyrus.jpg");
        UpdateUserDto updateUserDto = new UpdateUserDto(null, null, null,
                "I work at statefarm", "https://api.realworld.io/images/smiley-cyrus.jpg");

        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(user));
        when(userRepository.save(any(UserAccount.class))).thenAnswer(i -> i.getArguments()[0]);
        UserAccount result = userService.updateUser(updateUserDto, username);

        assertEquals(updatedUser, result);
    }

    @Test
    void testAddFollower_shouldReturnUser() {
        UserAccount updatedUser = user;
        UserAccount follower = new UserAccount("Jane", "", "jane@jane.jane", null, null);
        updatedUser.getFollowers().add(follower);

        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(user));
        when(userRepository.save(any(UserAccount.class))).thenAnswer(i -> i.getArguments()[0]);
        UserAccount result = userService.addFollower(username, follower.getUsername());

        assertEquals(updatedUser, result);
    }

    @Test
    void testDeleteFollower_shouldReturnUser() {
        UserAccount updatedUser = user;
        UserAccount follower = new UserAccount("Jane", "", "jane@jane.jane", null, null);
        updatedUser.getFollowers().add(follower);

        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(updatedUser));
        when(userRepository.save(any(UserAccount.class))).thenAnswer(i -> i.getArguments()[0]);
        UserAccount result = userService.deleteFollower(username, follower.getUsername());

        assertEquals(user, result);
    }
}
