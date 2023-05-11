package ru.yandex.practicum.filmorate.dao;

import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.util.List;

@Repository
public class FilmDaoImpl implements FilmDao {
    private FilmStorage filmStorage;

    public FilmDaoImpl(FilmStorage filmStorage) {
        this.filmStorage = filmStorage;
    }

    @Override
    public List<Film> getFilms() {
        return filmStorage.getFilms();
    }

    @Override
    public Film addFilm(Film film) {
        return filmStorage.addFilm(film);
    }

    @Override
    public Film updateFilm(Film film) {
        return filmStorage.updateFilm(film);
    }

    @Override
    public Film getFilm(int id) {
        return filmStorage.getFilm(id);
    }

    @Override
    public List<Film> getPopularFilms(int count) {
        return filmStorage.getPopularFilms(count);
    }

    @Override
    public boolean addGenreToFilm(int filmId, int genreId) {
        return filmStorage.addGenreToFilm(filmId, genreId);
    }

    @Override
    public boolean deleteGenreFromFilm(int filmId, int genreId) {
        return filmStorage.deleteGenreFromFilm(filmId, genreId);
    }

    @Override
    public boolean clearGenresFromFilm(int filmId) {
        return filmStorage.clearGenresFromFilm(filmId);
    }

    @Override
    public List<Integer> getLikesByFilm(int filmId) {
        return filmStorage.getLikesByFilm(filmId);
    }

    @Override
    public boolean addLike(int filmId, int userId) {
        return filmStorage.addLike(filmId, userId);
    }

    @Override
    public boolean deleteLike(int filmId, int userId) {
        return filmStorage.deleteLike(filmId, userId);
    }
}
