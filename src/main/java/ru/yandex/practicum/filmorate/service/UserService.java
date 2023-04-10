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
    private final UserStorage userStorage;

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
        User friend = userStorage.getUser(friendId);

        user.getFriends().add(friendId);
        friend.getFriends().add(id);

        log.info("Пользователь с id " + id + " добавил пользователя с id " + friendId + " в друзья");
        return user;
    }

    public User deleteFriend(int id, int friendId) {
        User user = userStorage.getUser(id);
        User friend = userStorage.getUser(friendId);

        user.getFriends().remove(friendId);
        friend.getFriends().remove(id);

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

    public List<User> getCommonFriends(int id, int otherId) {
        userStorage.getUser(id);
        userStorage.getUser(otherId);
        User user = userStorage.getUser(id);
        User friend = userStorage.getUser(otherId);
        return user.getFriends().stream()
                .filter(friend.getFriends()::contains)
                .map(userStorage::getUser)
                .collect(Collectors.toList());
    }
}
