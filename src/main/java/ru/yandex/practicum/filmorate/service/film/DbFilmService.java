package ru.yandex.practicum.filmorate.service.film;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.service.user.UserService;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("DbFilmService")
public class DbFilmService implements FilmService {
    private final FilmStorage filmStorage;
    private final UserService userService;

    @Autowired
    public DbFilmService(@Qualifier("FilmDbStorage") FilmStorage filmStorage,
                         @Qualifier("DbUserService") UserService userService) {
        this.filmStorage = filmStorage;
        this.userService = userService;
    }

    @Override
    public List<Film> getFilms() {
        List<Film> films = filmStorage.getFilms();
        Map<Integer, List<Genre>> genresByFilmId = new HashMap<>();

        for (Film film : films) {
            int filmId = film.getId();
            List<Genre> genres = filmStorage.getGenresByFilm(filmId);
            genresByFilmId.put(filmId, genres);
        }

        films.forEach(film -> {
            Integer filmId = film.getId();
            List<Genre> genres = genresByFilmId.get(filmId);
            genres.forEach(film::addGenre);
        });
        return films;
    }

    @Override
    public Film getFilm(int id) {
        Film film = filmStorage.getFilm(id);
        filmStorage.getGenresByFilm(film.getId())
                .forEach(film::addGenre);
        return film;
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
    public List<Film> getPopularFilms(Integer count) {
        // Получаем список популярных фильмов из filmStorage
        List<Film> popularFilms = filmStorage.getPopularFilms(count);

        // Получаем список всех жанров
        List<Genre> allGenres = filmStorage.getAllGenres();

        // Для каждого фильма выбираем соответствующие жанры из списка всех жанров
        for (Film film : popularFilms) {
            List<Genre> genres = new ArrayList<>();
            for (Genre genre : allGenres) {
                if (film.getGenres().contains(genre.getId())) {
                    genres.add(genre);
                }
            }
            for (Genre genre : genres) {
                film.addGenre(genre);
            }
        }

        return popularFilms;
    }

    @Override
    public List<Integer> addLike(int filmId, int userId) {
        getFilm(filmId);
        userService.getUser(userId);
        filmStorage.addLike(filmId, userId);
        return filmStorage.getLikesByFilm(filmId);
    }

    @Override
    public List<Integer> deleteLike(int filmId, int userId) {
        getFilm(filmId);
        userService.getUser(userId);
        filmStorage.deleteLike(filmId, userId);
        return filmStorage.getLikesByFilm(filmId);
    }
}