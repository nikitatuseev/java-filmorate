package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.UserException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class UserControllerTest {
    UserController controller = new UserController();

    @Test
    void register() {
        User user = new User(1, "dsd@mail.ru", "login1", "",
                LocalDate.of(1500, 1, 23));
        controller.register(user);
        assertEquals(user, controller.getUsers().get(0));
    }

    @Test
    void update() {
        User user = new User(1, "dsd@mail.ru", "login1", "",
                LocalDate.of(1500, 1, 23));
        controller.register(user);
        User updateUser = new User(1, "dsd@mail.ru", "login1", "name",
                LocalDate.of(1500, 1, 23));
        controller.updateUser(updateUser);
        assertEquals(updateUser, controller.getUsers().get(0));
    }

    @Test
    public void wrongBirthday() {
        User user = new User(1, "dsd@mail.ru", "login1", "", LocalDate.now().plusDays(1));
        assertThrows(UserException.class, () -> controller.checkDate(user));
    }

    @Test
    public void normalBirthday() {
        User user = new User(1, "dsd@mail.ru", "login1", "", LocalDate.now().minusYears(1));
        assertDoesNotThrow(() -> controller.checkDate(user));
    }
}
