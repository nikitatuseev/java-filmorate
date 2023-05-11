package ru.yandex.practicum.filmorate.dao.genre;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.FilmException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.StringJoiner;

@Repository
@Slf4j
public class GenreDaoImpl implements GenreDao {
    private final JdbcTemplate jdbcTemplate;

    public GenreDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Genre> getGenres() {
        String sql = "SELECT * FROM genre ORDER BY genre_id";
        return jdbcTemplate.query(sql, (rs, rowNum) -> mapRowToGenre(rs));
    }

    @Override
    public Genre getGenre(int id) {
        String sql = "SELECT * FROM genre WHERE genre_id = ?";
        try {
            return jdbcTemplate.queryForObject(sql, (rs, rowNum) -> mapRowToGenre(rs), id);
        } catch (DataRetrievalFailureException e) {
            log.warn("Жанр с id {} не найден", id);
            throw new FilmException("Жанр с id " + id + " не найден");
        }
    }

    @Override
    public List<Genre> getAllGenres(List<Film> films) {
        StringJoiner joiner = new StringJoiner(",");
        for (Film film : films) {
            joiner.add(String.valueOf(film.getId()));
        }
        String sql = "SELECT DISTINCT g.* FROM genre AS g " +
                "JOIN film_genre AS fg ON g.genre_id = fg.genre_id " +
                "WHERE fg.film_id IN (" + joiner.toString() + ") " +
                "ORDER BY g.genre_id";

        return jdbcTemplate.query(sql, (rs, rowNum) -> mapRowToGenre(rs));
    }

    @Override
    public List<Genre> getGenresByFilm(int id) {
        String sql = "SELECT g.* FROM genre AS g " +
                "JOIN film_genre AS fg ON g.genre_id = fg.genre_id " +
                "WHERE fg.film_id = ?";

        return jdbcTemplate.query(sql, (rs, rowNum) -> mapRowToGenre(rs), id);
    }

    private Genre mapRowToGenre(ResultSet rs) throws SQLException {
        int id = rs.getInt("genre_id");
        String name = rs.getString("name");
        return Genre.builder()
                .id(id)
                .name(name)
                .build();
    }
}
