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
        String sql = "SELECT u.*\n" +
                "FROM users u\n" +
                "LEFT JOIN friends f1 ON (u.user_id = f1.user_id AND f1.friend_id = ?)\n" +
                "LEFT JOIN friends f2 ON (u.user_id = f2.friend_id AND f2.user_id = ? AND f2.status = true)\n" +
                "WHERE f1.user_id IS NOT NULL OR f2.friend_id IS NOT NULL\n" +
                "  AND u.user_id <> ?";
        return jdbcTemplate.query(sql, new Object[]{id, id, id}, (rs, rowNum) ->
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
        String sql = "DELETE FROM friends WHERE (user_id = ? AND friend_id = ?) OR (user_id = ? AND friend_id = ?)";
        return jdbcTemplate.update(sql, userId, friendId, friendId, userId) > 0;
    }

    @Override
    public List<User> getCommonFriends(int id, int otherId) {
        List<User> friends1 = getFriendsByUser(id);
        List<User> friends2 = getFriendsByUser(otherId);

        friends1.retainAll(friends2);

        return friends1;
    }

//пытался использовать запрос с join но он всегда выводит пустой список
    /*@Override
    public List<User> getCommonFriends(int id, int otherId) {
        String sql = "SELECT u.*\n" +
                "FROM friends f1\n" +
                "LEFT JOIN friends f2 ON (f1.friend_id = f2.friend_id AND f2.user_id = ? AND f2.status = true)\n" +
                "LEFT JOIN users u ON (f1.friend_id = u.user_id)\n" +
                "WHERE f1.user_id = ?\n" +
                "  AND f2.friend_id IS NOT NULL\n" +
                "  AND u.user_id <> ?";
        return jdbcTemplate.query(sql, new Object[]{otherId, id, id}, (rs, rowNum) ->
                User.builder()
                        .id(rs.getInt("USER_ID"))
                        .email(rs.getString("EMAIL"))
                        .login(rs.getString("LOGIN"))
                        .name(rs.getString("NAME"))
                        .birthday(rs.getDate("BIRTHDAY").toLocalDate())
                        .build()
        );
    }
     */
}
