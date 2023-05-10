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
//я сделал новый метод чтобы он возвращал список пользователей и он работает но тесты не проходит и я
    //не понимаю в чем ошибка
    //и даже со старой реализацией теперь почему-то не проходит один тест
    /*@Override
    public List<User> getFriendsByUser(int id) {
        String sql = "SELECT u.* " +
                "FROM users u " +
                "WHERE u.user_id IN ( " +
                "  SELECT f.friend_id " +
                "  FROM friends f " +
                "  WHERE (f.user_id = ? OR f.friend_id = ?) " +
                "     AND f.status = true " +
                ") " +
                "AND u.user_id <> ? ";

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
     */

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
//тут тоже самое
    /*@Override
    public List<User> getCommonFriends(int id, int otherId) {
        String sql = "SELECT * FROM USERS u " +
                "WHERE u.USER_ID IN (" +
                "    SELECT f1.FRIEND_ID FROM FRIENDS f1 " +
                "    WHERE f1.USER_ID = ? AND f1.STATUS = true" +
                ") AND u.USER_ID IN (" +
                "    SELECT f2.FRIEND_ID FROM FRIENDS f2 " +
                "    WHERE f2.USER_ID = ? AND f2.STATUS = true" +
                ")";
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
     */
}
