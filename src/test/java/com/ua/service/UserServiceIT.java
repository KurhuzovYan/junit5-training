package com.ua.service;

import com.ua.dao.UserDao;
import com.ua.dto.CreateUserDto;
import com.ua.dto.UserDto;
import com.ua.entity.User;
import com.ua.integration.IntegrationTestBase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Optional;

import static com.ua.entity.Gender.MALE;
import static com.ua.entity.Role.ADMIN;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class UserServiceIT extends IntegrationTestBase {

    private UserDao userDao;
    private UserService userService;

    @BeforeEach
    void init() {
        userDao = UserDao.getInstance();
        userService = UserService.getInstance();
    }

    @Test
    void login() {
        User user = userDao.save(getUser("test@gmail.com"));

        Optional<UserDto> actualResult = userService.login(user.getEmail(), user.getPassword());

        assertThat(actualResult).isPresent();
        assertThat(actualResult.get().getId()).isEqualTo(user.getId());
    }

    @Test
    void create() {
        CreateUserDto createUserDto = getCreateUserDto();

        UserDto actualResult = userService.create(createUserDto);

        assertNotNull(actualResult.getId());
    }

    private static User getUser(String email) {
        return User.builder()
                .name("Yan")
                .email(email)
                .password("Qwerty")
                .role(ADMIN)
                .gender(MALE)
                .birthday(LocalDate.of(1996, 2, 13))
                .build();
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
}
