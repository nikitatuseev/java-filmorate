package ru.yandex.practicum.filmorate.storage.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.IncorrectParameterException;
import ru.yandex.practicum.filmorate.exception.UserException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class InMemoryUserStorage implements UserStorage {
    private final Map<Integer, User> users = new HashMap<>();
    private int idGenerator = 1;

    @Override
    public User create(User user) {
        checkDate(user);
        if (users.values().stream().noneMatch(u -> u.getLogin().equals(user.getLogin()))) {
            if (user.getName() == null || user.getName().isBlank()) {
                user.setName(user.getLogin());
            }
            user.setId(idGenerator++);
            users.put(user.getId(), user);
            log.info("Пользователь с логином {} добавлен", user.getLogin());
        }
        return user;
    }

    @Override
    public User update(User user) {
        checkDate(user);
        if (users.containsKey(user.getId())) {
            if (user.getName() == null || user.getName().isBlank()) {
                user.setName(user.getLogin());
            }
            users.put(user.getId(), user);
            log.info("пользователь с логином {} обновлен", user.getLogin());
            return user;
        } else {
            log.error("Пользователь с id = {} не найден", user.getId());
            throw new UserException("Пользователь с таким id не существует");
        }
    }

    @Override
    public User delete(User user) {
        if (users.containsKey(idGenerator)) {
            users.remove(user.getId());
            return user;
        } else {
            throw new UserException("Пользователь с таким id не существует");
        }
    }

    @Override
    public User getUser(int id) {
        if (users.containsKey(id)) {
            return users.get(id);
        } else {
            throw new UserException("Пользователь с таким id не существует");
        }
    }

    @Override
    public List<User> getUsers() {
        return new ArrayList<>(users.values());
    }

    public void checkDate(User user) {
        if (user.getBirthday().isAfter(LocalDate.now())) {
            log.error("дата рождения не может быть в будущем.");
            throw new IncorrectParameterException("birthday");
        }
    }
}
