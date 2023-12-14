package com.ua.validator;

import com.ua.dto.CreateUserDto;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.ua.entity.Gender.MALE;
import static com.ua.entity.Role.ADMIN;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CreateUserValidatorTest {

    private final CreateUserValidator validator = CreateUserValidator.getInstance();

    @Test
    void shouldPassValidation() {
        CreateUserDto dto = CreateUserDto.builder()
                .name("Yan")
                .email("yan@gmail.com")
                .password("Qwerty123")
                .birthday("1996-02-13")
                .role(ADMIN.name())
                .gender(MALE.name())
                .build();

        ValidationResult actualResult = validator.validate(dto);

        assertTrue(actualResult.isValid());
    }

    @Test
    void invalidBirthday() {
        CreateUserDto dto = CreateUserDto.builder()
                .name("Yan")
                .email("yan@gmail.com")
                .password("Qwerty123")
                .birthday("1996-02-13 14:34")
                .role(ADMIN.name())
                .gender(MALE.name())
                .build();

        ValidationResult actualResult = validator.validate(dto);

        assertThat(actualResult.getErrors()).hasSize(1);
        assertThat(actualResult.getErrors().get(0).getCode()).isEqualTo("invalid.birthday");
    }

    @Test
    void invalidGender() {
        CreateUserDto dto = CreateUserDto.builder()
                .name("Yan")
                .email("yan@gmail.com")
                .password("Qwerty123")
                .birthday("1996-02-13")
                .role(ADMIN.name())
                .gender("fake")
                .build();

        ValidationResult actualResult = validator.validate(dto);

        assertThat(actualResult.getErrors()).hasSize(1);
        assertThat(actualResult.getErrors().get(0).getCode()).isEqualTo("invalid.gender");
    }

    @Test
    void invalidRole() {
        CreateUserDto dto = CreateUserDto.builder()
                .name("Yan")
                .email("yan@gmail.com")
                .password("Qwerty123")
                .birthday("1996-02-13")
                .role("fake")
                .gender(MALE.name())
                .build();

        ValidationResult actualResult = validator.validate(dto);

        assertThat(actualResult.getErrors()).hasSize(1);
        assertThat(actualResult.getErrors().get(0).getCode()).isEqualTo("invalid.role");
    }

    @Test
    void invalidRoleBirthdayGender() {
        CreateUserDto dto = CreateUserDto.builder()
                .name("Yan")
                .email("yan@gmail.com")
                .password("Qwerty123")
                .birthday("1996-02-13 14:30")
                .role("fake role")
                .gender("fake gender")
                .build();

        ValidationResult actualResult = validator.validate(dto);

        List<String> errorsList = actualResult.getErrors().stream()
                .map(Error::getCode)
                .toList();

        assertThat(actualResult.getErrors()).hasSize(3);
        assertThat(errorsList).contains("invalid.role", "invalid.gender", "invalid.birthday");
    }

}