package ru.yandex.practicum.filmorate.service.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.friends.FriendsDao;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.List;

@Service("DbUserService")
@Slf4j
public class DbUserService implements UserService {
    private final UserStorage userStorage;
    private final FriendsDao friendsDao;

    public DbUserService(@Qualifier("UserDbStorage") UserStorage userStorage, FriendsDao friendsDao) {
        this.userStorage = userStorage;
        this.friendsDao = friendsDao;
    }

    @Override
    public List<User> getUsers() {
        return userStorage.getUsers();
    }

    @Override
    public User getUser(int id) {
        return userStorage.getUser(id);
    }

    @Override
    public User addUser(User user) {
        return userStorage.create(user);
    }

    @Override
    public User updateUser(User user) {
        return userStorage.updateUser(user);
    }

    @Override
    public List<User> getListOfFriends(int id) {
        return friendsDao.getFriendsByUser(id);
    }

    /* @Override
     public List<User> getListOfFriends(int id) {
         return friendsDao.getFriendsByUser(id).stream()
                 .map(this::getUser)
                 .collect(Collectors.toList());
     }

     */
    @Override
    public List<User> getCommonFriends(int id, int otherId) {
        return friendsDao.getCommonFriends(id, otherId);
    }

   /* @Override
    public List<User> getCommonFriends(int id, int otherId) {
        List<Integer> friends = friendsDao.getFriendsByUser(id);
        List<Integer> otherFriends = friendsDao.getFriendsByUser(otherId);
        return friends.stream()
                .filter(otherFriends::contains)
                .map(this::getUser)
                .collect(Collectors.toList());
    }
    */

    @Override
    public void addFriend(int id, int friendId) {
        if (!areFriends(id, friendId)) {
            friendsDao.addFriend(id, friendId);
        } else if (areFriends(id, friendId)) {
            friendsDao.updateFriend(friendId, id, true);
        }
    }

    private boolean areFriends(int id, int friendId) {
        User user = userStorage.getUser(id);
        User friend = userStorage.getUser(friendId);
        boolean isUserHasFriend = user.getFriends().contains(friendId);
        boolean isFriendHasUser = friend.getFriends().contains(id);
        return isUserHasFriend && isFriendHasUser;
    }

    @Override
    public void deleteFriend(int id, int friendId) {
        User user = getUser(id);
        User friend = getUser(friendId);
        friendsDao.deleteFriend(id, friendId);
    }
}