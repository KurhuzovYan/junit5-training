package com.ua.dao;

import com.ua.entity.User;
import com.ua.integration.IntegrationTestBase;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static com.ua.entity.Gender.MALE;
import static com.ua.entity.Role.ADMIN;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

class UserDaoIT extends IntegrationTestBase {

    private final UserDao userDao = UserDao.getInstance();

    @Test
    void findAll() {
        User user1 = userDao.save(getUser("test1@gmail.com"));
        User user2 = userDao.save(getUser("test2@gmail.com"));
        User user3 = userDao.save(getUser("test3@gmail.com"));

        List<User> actualResult = userDao.findAll();

        List<Integer> userIds = actualResult.stream()
                .map(User::getId)
                .toList();

        assertThat(actualResult).hasSize(8);
        assertThat(userIds).contains(user1.getId(), user2.getId(), user3.getId());
    }

    @Test
    void findById() {
        User user = userDao.save(getUser("test@gmail.com"));

        Optional<User> actualResult = userDao.findById(user.getId());

        assertThat(actualResult).isPresent();
        assertThat(actualResult.get()).isEqualTo(user);
    }

    @Test
    void save() {
        User user = getUser("test@gmail.com");

        User actualResult = userDao.save(user);

        assertNotNull(actualResult.getId());
    }

    @Test
    void findByEmailAndPasswordSuccess() {
        User user = userDao.save(getUser("test@gmail.com"));

        Optional<User> actualResult = userDao.findByEmailAndPassword(user.getEmail(), user.getPassword());

        assertThat(actualResult).isPresent();
        assertThat(actualResult.get()).isEqualTo(user);
    }

    @Test
    void findByEmailAndPasswordFailed() {
        userDao.save(getUser("test@gmail.com"));

        Optional<User> actualResult = userDao.findByEmailAndPassword("dummy", "dummy");

        assertThat(actualResult).isEmpty();
    }

    @Test
    void deleteExistingEntity() {
        User user = userDao.save(getUser("test@gmail.com"));

        boolean actualResult = userDao.delete(user.getId());

        assertTrue(actualResult);
    }

    @Test
    void deleteNotExistingEntity() {
        userDao.save(getUser("test@gmail.com"));

        boolean actualResult = userDao.delete(any());

        assertFalse(actualResult);
    }

    @Test
    void update() {
        User user = getUser("test@gmail.com");
        userDao.save(user);
        user.setName("Nikolay");
        user.setRole(ADMIN);

        userDao.update(user);

        User updatedUser = userDao.findById(user.getId()).get();
        assertThat(updatedUser).isEqualTo(user);
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
}