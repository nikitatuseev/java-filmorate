package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.UserException;
import ru.yandex.practicum.filmorate.model.CreateGroup;
import ru.yandex.practicum.filmorate.model.UpdateGroup;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {
    private final Map<Integer, User> users = new HashMap<>();
    private int idGenerator = 1;

    @PostMapping()
    public User register(@RequestBody @Validated(CreateGroup.class) User user) {
        if (user.getBirthday().isAfter(LocalDate.now())) {
            log.error("дата рождения не может быть в будущем.");
            throw new UserException("дата рождения не может быть в будущем.");
        }
        if (users.values().stream().noneMatch(u -> u.getLogin().equals(user.getLogin()))) {
            if (user.getName().isBlank()) {
                user.setName(user.getLogin());
            }
            user.setId(idGenerator++);
            users.put(user.getId(), user);
            log.info("Пользователь с логином {} добавлен", user.getLogin());
            return user;
        } else {
            log.error("Пользователь с логином {} уже существует", user.getLogin());
            throw new UserException("Пользователь с таким логином уже существует");
        }
    }

    @GetMapping()
    public List<User> getUsers() {
        return new ArrayList<>(users.values());
    }

    @PutMapping
    public User updateUser(@RequestBody @Validated(UpdateGroup.class) User user) {
        if (user.getBirthday().isAfter(LocalDate.now())) {
            log.error("дата рождения не может быть в будущем.");
            throw new UserException("дата рождения не может быть в будущем.");
        }
        if (users.containsKey(user.getId())) {
            if (user.getName().isBlank()) {
                user.setName(user.getLogin());
            }
            users.put(user.getId(), user);
            return user;
        } else {
            log.error("Пользователь с id = {} не найден", user.getId());
            throw new RuntimeException("Пользователь с таким id не существует");
        }
    }
}
