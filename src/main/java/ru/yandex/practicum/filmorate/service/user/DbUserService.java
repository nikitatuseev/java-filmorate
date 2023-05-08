package ru.yandex.practicum.filmorate.service.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.friends.FriendsDao;
import ru.yandex.practicum.filmorate.exception.UserException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.List;
import java.util.stream.Collectors;

@Service("DbUserService")
@Slf4j
public class DbUserService implements UserService {
    private final UserStorage storage;
    private final FriendsDao friendsDao;

    public DbUserService(@Qualifier("UserDbStorage") UserStorage storage, FriendsDao friendsDao) {
        this.storage = storage;
        this.friendsDao = friendsDao;
    }

    @Override
    public List<User> getUsers() {
        return storage.getUsers();
    }

    @Override
    public User getUser(int id) {
        return storage.getUser(id);
    }

    @Override
    public User addUser(User user) {
        return storage.create(user);
    }

    @Override
    public User updateUser(User user) {
        return storage.updateUser(user);
    }

    @Override
    public List<User> getListOfFriends(int id) {
        return friendsDao.getFriendsByUser(id).stream()
                .map(this::getUser)
                .collect(Collectors.toList());
    }

    @Override
    public List<User> getCommonFriends(int id, int otherId) {
        List<Integer> friends = friendsDao.getFriendsByUser(id);
        List<Integer> otherFriends = friendsDao.getFriendsByUser(otherId);
        return friends.stream()
                .filter(otherFriends::contains)
                .map(this::getUser)
                .collect(Collectors.toList());
    }

    @Override
    public List<Integer> addFriend(int id, int friendId) {
        if (!areFriends(id, friendId)) {
            friendsDao.addFriend(id, friendId);
        } else if (areFriends(id, friendId)) {
            friendsDao.updateFriend(friendId, id, true);
        }
        return friendsDao.getFriendsByUser(id);
    }

    private boolean areFriends(int id, int friendId) {
        getUser(id);
        getUser(friendId);
        boolean isUserHasFriend = friendsDao.getFriendsByUser(id).contains(friendId);
        boolean isFriendHasUser = friendsDao.getFriendsByUser(friendId).contains(id);
        return isUserHasFriend && isFriendHasUser;
    }

    //можно ли как-то упростить? я патылся упростить используя метод areFriends но из-за этого падали тесты
    @Override
    public List<Integer> deleteFriend(int id, int friendId) {
        getUser(id);
        getUser(friendId);
        boolean isUserHasFriend = friendsDao.getFriendsByUser(id).contains(friendId);
        boolean isFriendHasUser = friendsDao.getFriendsByUser(friendId).contains(id);
        if (!isUserHasFriend) {
            log.warn("Пользователь c id {} не друг пользователя c id {}", friendId, id);
            throw new UserException(
                    "Пользователь c id " + friendId + " не друг пользователя c id " + id);
        } else if (!isFriendHasUser) {
            friendsDao.deleteFriend(friendId, id);
        } else {
            if (!friendsDao.updateFriend(id, friendId, false)) {
                friendsDao.deleteFriend(friendId, id);
                friendsDao.addFriend(id, friendId);
            }
        }
        return friendsDao.getFriendsByUser(id);
    }
}
