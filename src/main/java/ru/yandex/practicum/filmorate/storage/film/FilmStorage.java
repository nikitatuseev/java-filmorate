package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;

public interface FilmStorage {
    List<Film> getFilms();

    Film addFilm(Film film);

    Film updateFilm(Film film);

    Film getFilm(int id);

    List<Film> getPopularFilms(int count);

    boolean addGenreToFilm(int filmId, int genreId);

    boolean deleteGenreFromFilm(int filmId, int genreId);

    boolean clearGenresFromFilm(int filmId);

    List<Integer> getLikesByFilm(int filmId);

    boolean addLike(int filmId, int userId);

    boolean deleteLike(int filmId, int userId);

    List<Genre> getAllGenres();

    List<Genre> getGenresByFilm(int id);
}
