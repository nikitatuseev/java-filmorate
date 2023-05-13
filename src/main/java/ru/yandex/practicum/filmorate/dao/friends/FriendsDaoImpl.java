package ru.yandex.practicum.filmorate.dao.friends;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

@Repository
public class FriendsDaoImpl implements FriendsDao {
    private final JdbcTemplate jdbcTemplate;


    public FriendsDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<User> getFriendsByUser(int id) {
        String sql = "SELECT * FROM users u JOIN friends f ON u.user_id = f.friend_id WHERE f.user_id = ?";
        return jdbcTemplate.query(sql, new Object[]{id}, (rs, rowNum) ->
                User.builder()
                        .id(rs.getInt("USER_ID"))
                        .email(rs.getString("EMAIL"))
                        .login(rs.getString("LOGIN"))
                        .name(rs.getString("NAME"))
                        .birthday(rs.getDate("BIRTHDAY").toLocalDate())
                        .build()
        );
    }

    @Override
    public boolean addFriend(int userId, int friendId) {
        String sql = "INSERT INTO  friends(friend_id, user_id, status) " +
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
        String sql = "DELETE FROM friends WHERE (user_id = ? AND friend_id = ?) OR (user_id = ? AND friend_id = ?)";
        return jdbcTemplate.update(sql, userId, friendId, friendId, userId) > 0;
    }

    @Override
    public List<User> getCommonFriends(int id, int otherId) {
        String sql = "SELECT * FROM users u JOIN friends f ON u.user_id = f.friend_id AND f.user_id = ? JOIN friends o ON u.user_id = o.friend_id AND o.user_id = ?";
        return jdbcTemplate.query(sql, new Object[]{id, otherId}, (rs, rowNum) ->
                User.builder()
                        .id(rs.getInt("USER_ID"))
                        .email(rs.getString("EMAIL"))
                        .login(rs.getString("LOGIN"))
                        .name(rs.getString("NAME"))
                        .birthday(rs.getDate("BIRTHDAY").toLocalDate())
                        .build()
        );
    }
}
