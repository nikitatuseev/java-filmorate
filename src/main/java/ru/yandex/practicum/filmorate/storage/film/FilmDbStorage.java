package ru.yandex.practicum.filmorate.storage.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.FilmException;
import ru.yandex.practicum.filmorate.exception.IncorrectParameterException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

@Repository("FilmDbStorage")
@Slf4j
public class FilmDbStorage implements FilmStorage {
    private static final LocalDate FILM_BIRTHDAY = LocalDate.of(1895, 12, 28);
    private final JdbcTemplate jdbcTemplate;

    public FilmDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Film> getFilms() {
        String sql = "SELECT f.*, m.name AS mpa_name FROM films AS f JOIN mpa AS m ON f.mpa_id = m.mpa_id";
        return jdbcTemplate.query(sql, (rs, rowNum) -> buildFilmFromResultSet(rs));
    }

    @Override
    public Film getFilm(int id) {
        String sql = "SELECT f.*, m.name AS mpa_name FROM films AS f JOIN mpa AS m ON f.mpa_id = m.mpa_id WHERE f.film_id = ?";
        try {
            return jdbcTemplate.queryForObject(sql, (rs, rowNum) -> buildFilmFromResultSet(rs), id);
        } catch (DataRetrievalFailureException e) {
            log.warn("Фильм с id {} не найден", id);
            throw new FilmException("Фильм с id " + id + " не найден");
        }
    }

    @Override
    public Film addFilm(Film film) {
        checkDate(film);
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate).withTableName("films").usingGeneratedKeyColumns("film_id");
        int id = simpleJdbcInsert.executeAndReturnKey(film.toMap()).intValue();
        film.setId(id);
        film.getGenres().forEach(genre -> addGenreToFilm(id, genre.getId()));
        log.debug("Фильм {} сохранен", film);
        return film;
    }

    @Override
    public Film updateFilm(Film film) {
        checkDate(film);
        String sql = "UPDATE films SET name=?, description=?, release_date=?, duration=?, mpa_id=? WHERE film_id=?";
        int rowsUpdated = jdbcTemplate.update(sql, film.getName(), film.getDescription(), film.getReleaseDate(), film.getDuration(), film.getMpa().getId(), film.getId());
        if (rowsUpdated == 0) {
            log.warn("Фильм с id {} не найден", film.getId());
            throw new FilmException("Фильм с id " + film.getId() + " не найден");
        }
        clearGenresFromFilm(film.getId());
        film.getGenres().forEach(genre -> addGenreToFilm((int) film.getId(), genre.getId()));
        return film;
    }

    public List<Film> getPopularFilms(int count) {
        String sql = "SELECT f.*, m.name AS mpa_name FROM films AS f " +
                "JOIN mpa AS m ON f.mpa_id = m.mpa_id " +
                "LEFT JOIN  " +
                "(SELECT film_id, COUNT(user_id) AS likes_qty FROM likes GROUP BY film_id ORDER BY likes_qty DESC limit ?) " +
                "AS top ON f.film_id = top.film_id " +
                "ORDER BY top.likes_qty DESC " +
                "limit ?";
        return jdbcTemplate.query(sql, (rs, rowNum) -> buildFilmFromResultSet(rs), count, count);
    }

    @Override
    public List<Genre> getAllGenres() {
        String sql = "SELECT * FROM genre ORDER BY genre_id";
        return jdbcTemplate.query(sql, (rs, rowNum) -> mapRowToGenre(rs));
    }

    @Override
    public List<Genre> getGenresByFilm(int id) {
        String sql = "SELECT g.* FROM genre AS g " +
                "WHERE g.genre_id IN (SELECT fg.genre_id FROM film_genre AS fg WHERE fg.film_id = ?) " +
                "ORDER BY g.genre_id";

        return jdbcTemplate.query(sql, (rs, rowNum) -> mapRowToGenre(rs), id);
    }

    @Override
    public boolean addGenreToFilm(int filmId, int genreId) {
        String sql = "INSERT INTO  film_genre(film_id, genre_id) " +
                "VALUES (?, ?)";
        return jdbcTemplate.update(sql, filmId, genreId) > 0;
    }

    @Override
    public boolean deleteGenreFromFilm(int filmId, int genreId) {
        String sql = "DELETE FROM film_genre WHERE (film_id = ? AND genre_id = ?)";
        return jdbcTemplate.update(sql, filmId, genreId) > 0;
    }

    @Override
    public boolean clearGenresFromFilm(int filmId) {
        String sql = "DELETE FROM film_genre WHERE film_id = ?";
        return jdbcTemplate.update(sql, filmId) > 0;
    }

    @Override
    public List<Integer> getLikesByFilm(int filmId) {
        String sql = "SELECT user_id FROM likes WHERE film_id =?";
        return jdbcTemplate.query(sql, (rs, rowNum) -> rs.getInt("user_id"), filmId);
    }

    @Override
    public boolean addLike(int filmId, int userId) {
        String sql = "INSERT INTO likes(film_id, user_id) " +
                "VALUES (?, ?)";
        return jdbcTemplate.update(sql, filmId, userId) > 0;
    }

    @Override
    public boolean deleteLike(int filmId, int userId) {
        String sql = "delete from likes where (film_id = ? AND user_id = ?)";
        return jdbcTemplate.update(sql, filmId, userId) > 0;
    }

    protected void checkDate(Film film) {
        if (film.getReleaseDate().isBefore(FILM_BIRTHDAY)) {
            log.error("дата релиза не может быть раньше 28 декабря 1895 года.");
            throw new IncorrectParameterException("releaseDate");
        }
    }

    private Film buildFilmFromResultSet(ResultSet rs) throws SQLException {
        Mpa mpa = Mpa.builder()
                .id(rs.getInt("mpa_id"))
                .name(rs.getString("mpa_name"))
                .build();

        return Film.builder()
                .id(rs.getInt("film_id"))
                .name(rs.getString("name"))
                .description(rs.getString("description"))
                .releaseDate(rs.getDate("release_date").toLocalDate())
                .duration(rs.getInt("duration"))
                .mpa(mpa)
                .build();
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
