package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {
    @InjectMocks
    private UserService userService;

    @Mock
    private InMemoryUserStorage userStorage;

    private final Set<Integer> emptySet = new HashSet<>();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetUsers() {
        List<User> users = new ArrayList<>();
        User user1 = new User(1, "dd@mail.ru", "dc", "user1", LocalDate.of(1990, 2, 1),
                emptySet);
        User user2 = new User(1, "dd@mail.ru", "d", "user2", LocalDate.of(1990, 2, 1),
                emptySet);
        users.add(user1);
        users.add(user2);

        when(userStorage.getUsers()).thenReturn(users);

        List<User> result = userService.getUsers();

        assertEquals(users, result);
    }

    @Test
    void testGetUser() {
        User user = new User(1, "dd@mail.ru", "dc", "user1", LocalDate.of(1990, 2, 1),
                emptySet);

        when(userStorage.getUser(1)).thenReturn(user);

        User result = userService.getUser(1);

        assertEquals(user, result);
    }

    @Test
    void testCreate() {
        User user = new User(1, "dd@mail.ru", "dc", "user1", LocalDate.of(1990, 2, 1),
                emptySet);

        when(userStorage.create(user)).thenReturn(user);

        User result = userService.create(user);

        assertEquals(user, result);
    }

    @Test
    void testUpdate() {
        User user = new User(1, "dd@mail.ru", "dc", "user1", LocalDate.of(1990, 2, 1),
                emptySet);

        when(userStorage.update(user)).thenReturn(user);

        User result = userService.update(user);

        assertEquals(user, result);
    }

    @Test
    void testAddFriend() {
        User user1 = new User(1, "dd@mail.ru", "dc", "user1", LocalDate.of(1990, 2, 1),
                emptySet);
        User user2 = new User(2, "dd@mail.ru", "d", "user2", LocalDate.of(1990, 2, 1),
                emptySet);

        when(userStorage.getUser(1)).thenReturn(user1);
        when(userStorage.getUser(2)).thenReturn(user2);

        User result = userService.addFriend(1, 2);

        Assertions.assertTrue(user1.getFriends().contains(2));
        Assertions.assertTrue(user2.getFriends().contains(1));
        Assertions.assertEquals(user1, result);
    }


    @Test
    void testDeleteFriend() {
        User user1 = new User(1, "dd@mail.ru", "dc", "user1", LocalDate.of(1990, 2, 1),
                emptySet);
        User user2 = new User(1, "dd@mail.ru", "d", "user2", LocalDate.of(1990, 2, 1),
                emptySet);
        user1.getFriends().add(2);
        user2.getFriends().add(1);

        when(userStorage.getUser(1)).thenReturn(user1);
        when(userStorage.getUser(2)).thenReturn(user2);

        User result = userService.deleteFriend(1, 2);

        Assertions.assertFalse(user1.getFriends().contains(2));
        Assertions.assertFalse(user2.getFriends().contains(1));
        assertEquals(user1, result);
    }

    @Test
    void testGetAllFriends() {
        User user1 = new User(1, "dd@mail.ru", "dc", "user1", LocalDate.of(1990, 2, 1),
                emptySet);
        User user2 = new User(2, "dd@mail.ru", "d", "user2", LocalDate.of(1990, 2, 1),
                emptySet);

        when(userStorage.getUser(1)).thenReturn(user1);
        when(userStorage.getUser(2)).thenReturn(user2);

        User result = userService.addFriend(1, 2);

        Set<User> res = new HashSet<>();
        res.add(result);

        Set<User> friends = new HashSet<>();
        friends.add(user1);

        Assertions.assertEquals(friends, res);
    }
}

