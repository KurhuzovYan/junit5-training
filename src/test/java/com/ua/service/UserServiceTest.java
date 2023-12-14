package com.ua.service;

import com.ua.dao.UserDao;
import com.ua.dto.CreateUserDto;
import com.ua.dto.UserDto;
import com.ua.entity.User;
import com.ua.exception.ValidationException;
import com.ua.mapper.CreateUserMapper;
import com.ua.mapper.UserMapper;
import com.ua.validator.CreateUserValidator;
import com.ua.validator.Error;
import com.ua.validator.ValidationResult;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static com.ua.entity.Gender.MALE;
import static com.ua.entity.Role.ADMIN;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private CreateUserValidator userValidator;
    @Mock
    private UserDao userDao;
    @Mock
    private CreateUserMapper createUserMapper;
    @Mock
    private UserMapper userMapper;
    @InjectMocks
    private UserService userService;

    @Test
    void loginSuccess() {
        //given
        User user = getUser();
        UserDto userDto = getUserDto();

        doReturn(Optional.of(user)).when(userDao).findByEmailAndPassword(user.getEmail(), user.getPassword());
        doReturn(userDto).when(userMapper).map(user);

        //when
        Optional<UserDto> actualResult = userService.login(user.getEmail(), user.getPassword());

        //then
        assertThat(actualResult).isPresent();
        assertThat(actualResult.get()).isEqualTo(userDto);
    }

    @Test
    void loginFailed() {
        //given
        doReturn(Optional.empty()).when(userDao).findByEmailAndPassword(any(), any());

        //when
        Optional<UserDto> actualResult = userService.login("dummy", "dummy");

        //then
        assertThat(actualResult).isEmpty();
        verifyNoInteractions(userMapper);
    }

    @Test
    void create() {
        //given
        CreateUserDto createUserDto = getCreateUserDto();
        User user = getUser();
        UserDto userDto = getUserDto();

        doReturn(new ValidationResult()).when(userValidator).validate(createUserDto);
        doReturn(user).when(createUserMapper).map(createUserDto);
        doReturn(userDto).when(userMapper).map(user);

        //when
        UserDto actualResult = userService.create(createUserDto);

        //then
        assertThat(actualResult).isEqualTo(userDto);
        verify(userDao).save(user);
    }

    @Test
    void shouldThrowExceptionIfDtoInvalid() {
        //given
        CreateUserDto createUserDto = getCreateUserDto();

        ValidationResult validationResult = new ValidationResult();
        validationResult.add(Error.of("invalid.birthday", "message"));

        doReturn(validationResult).when(userValidator).validate(createUserDto);

        //when
        assertThrows(ValidationException.class, ()-> userService.create(createUserDto));
        //then
        verifyNoInteractions(userDao, createUserMapper, userMapper);
    }

    private static CreateUserDto getCreateUserDto() {
        return CreateUserDto.builder()
                .name("Yan")
                .email("yan@gmail.com")
                .password("Qwerty")
                .birthday("1996-02-13")
                .role(ADMIN.name())
                .gender(MALE.name())
                .build();
    }

    private static UserDto getUserDto() {
        return UserDto.builder()
                .id(1)
                .name("Yan")
                .email("yan@gmail.com")
                .role(ADMIN)
                .gender(MALE)
                .birthday(LocalDate.of(1996, 2, 13))
                .build();
    }

    private static User getUser() {
        return User.builder()
                .id(1)
                .name("Yan")
                .email("yan@gmail.com")
                .password("Qwerty")
                .role(ADMIN)
                .gender(MALE)
                .birthday(LocalDate.of(1996, 2, 13))
                .build();
    }
}