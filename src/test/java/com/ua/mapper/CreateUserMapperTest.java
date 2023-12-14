package com.ua.mapper;

import com.ua.dto.CreateUserDto;
import com.ua.entity.User;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static com.ua.entity.Gender.MALE;
import static com.ua.entity.Role.ADMIN;
import static org.assertj.core.api.Assertions.assertThat;

class CreateUserMapperTest {

    private final CreateUserMapper userMapper = CreateUserMapper.getInstance();

    @Test
    void map() {
        CreateUserDto dto = CreateUserDto.builder()
                .name("Yan")
                .email("yan@gmail.com")
                .password("Qwerty123")
                .birthday("1996-02-13")
                .role(ADMIN.name())
                .gender(MALE.name())
                .build();

        User actualResult = userMapper.map(dto);

        User expectedResult = User.builder()
                .name("Yan")
                .email("yan@gmail.com")
                .password("Qwerty123")
                .role(ADMIN)
                .gender(MALE)
                .birthday(LocalDate.of(1996, 2, 13))
                .build();
        assertThat(actualResult).isEqualTo(expectedResult);
    }
}