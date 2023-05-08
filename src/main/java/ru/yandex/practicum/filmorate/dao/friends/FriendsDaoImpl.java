package ru.yandex.practicum.filmorate.dao.friends;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;


import java.util.List;

@Repository
public class FriendsDaoImpl implements FriendsDao {
    private final JdbcTemplate jdbcTemplate;

    public FriendsDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Integer> getFriendsByUser(int id) {
        String sql = "SELECT friend_id FROM friends WHERE user_id =? AND status = true " +
                "UNION SELECT user_id FROM friends WHERE friend_id = ?";
        return jdbcTemplate.query(sql, (rs, rowNum) -> rs.getInt("friend_id"), id, id);
    }

    @Override
    public boolean addFriend(int userId, int friendId) {
        String sql = "INSERT INTO  friends(user_id, friend_id, status) " +
                "VALUES (?, ?, false)";
        return jdbcTemplate.update(sql, friendId, userId) > 0;
    }

    @Override
    public boolean updateFriend(int userId, int friendId, boolean status) {
        String sql = "UPDATE friends SET status = ? " +
                "WHERE user_id = ? AND friend_id = ?";
        return jdbcTemplate.update(sql, status, userId, friendId) > 0;
    }

    @Override
    public boolean deleteFriend(int userId, int friendId) {
        String sql = "DELETE FROM friends WHERE (user_id = ? AND friend_id = ?)";
        return jdbcTemplate.update(sql, userId, friendId) > 0;
    }
}
