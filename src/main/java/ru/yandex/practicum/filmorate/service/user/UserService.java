package ru.yandex.practicum.filmorate.service.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserService {
    List<User> getUsers();

    User getUser(int id);

    User addUser(User user);

    User updateUser(User user);

    List<User> getListOfFriends(int id);

    List<User> getCommonFriends(int id, int otherId);

    void addFriend(int id, int friendId);

    void deleteFriend(int id, int friendId);
}

