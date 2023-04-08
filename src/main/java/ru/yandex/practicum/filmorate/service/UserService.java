package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.UserException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
public class UserService {
    UserStorage userStorage;

    @Autowired
    public UserService(InMemoryUserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public List<User> getUsers() {
        return userStorage.getUsers();
    }

    public User getUser(int id) {
        return userStorage.getUser(id);
    }

    public User create(User user) {
        return userStorage.create(user);
    }

    public User update(User user) {
        return userStorage.update(user);
    }

    public User addFriend(int id, int friendId) {
        User user = userStorage.getUser(id);
        if (user == null) {
            log.error("Пользователя с id {} не существует", id);
            throw new UserException("Пользователя с id " + id + " не существует");
        }

        User friend = userStorage.getUser(friendId);
        if (friend == null) {
            log.error("Пользователя с id {} не существует", friendId);
            throw new UserException("Пользователя с id " + friendId + " не существует");
        }

        user.getFriends().add(friendId);
        friend.getFriends().add(id);

        log.info("Пользователь с id " + id + " добавил пользователя с id " + friendId + " в друзья");

        userStorage.update(user);
        userStorage.update(friend);

        return user;
    }

    public User deleteFriend(int id, int friendId) {
        User user = userStorage.getUser(id);
        User friend = userStorage.getUser(friendId);

        if (user == null) {
            throw new UserException("Пользователя с id " + id + " не существует");
        }
        if (friend == null) {
            throw new UserException("Пользователя с id " + friendId + " не существует");
        }

        user.getFriends().remove(friendId);
        friend.getFriends().remove(id);

        update(user);
        update(friend);

        log.info("Пользователь с id {} удалил пользователя с id {} из друзей", id, friendId);

        return user;
    }

    public List<User> getAllFriends(User user) {
        if (!userStorage.getUsers().contains(user)) {
            log.error("Пользователя с id {} не существует ", user.getId());
            throw new UserException("Пользователя с id" + user.getId() + "не существует ");
        }
        List<User> friends = new ArrayList<>();
        Set<Integer> friendIds = user.getFriends();
        for (Integer friendId : friendIds) {
            User friend = userStorage.getUser(friendId);
            friends.add(friend);
        }
        return friends;
    }

    public Set<User> getCommonFriends(int id, int otherId) {
        if (userStorage.getUser(id) == null) {
            log.error("Пользователя с id {} не существует ", id);
            throw new UserException("Пользователя с id" + id + "не существует ");
        }
        if (userStorage.getUser(otherId) == null) {
            log.error("Пользователя с id {} не существует ", otherId);
            throw new UserException("Пользователя с id" + otherId + "не существует ");
        }
        User user = userStorage.getUser(id);
        User friend = userStorage.getUser(otherId);
        return user.getFriends().stream()
                .filter(friend.getFriends()::contains)
                .map(userStorage::getUser)
                .collect(Collectors.toSet());
    }
}
