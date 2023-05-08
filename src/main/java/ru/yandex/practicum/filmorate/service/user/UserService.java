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

    List<Integer> addFriend(int id, int friendId);

    List<Integer> deleteFriend(int id, int friendId);
}

