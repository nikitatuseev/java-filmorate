package ru.yandex.practicum.filmorate.dao.friends;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface FriendsDao {
    List<Integer> getFriendsByUser(int id);

    boolean addFriend(int userId, int friendId);

    boolean updateFriend(int userId, int friendId, boolean status);

    boolean deleteFriend(int userId, int friendId);

    // List<User> getCommonFriends(int id, int otherId);
}
