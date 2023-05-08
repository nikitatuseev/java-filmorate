package ru.yandex.practicum.filmorate.service.film;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.genre.GenreDao;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.user.UserService;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.util.List;
import java.util.stream.Collectors;

@Service("DbFilmService")
public class DbFilmService implements FilmService {
    private final FilmStorage filmStorage;
    private final GenreDao genreDao;
    private final UserService userService;

    @Autowired
    public DbFilmService(@Qualifier("FilmDbStorage") FilmStorage filmStorage,
                         GenreDao genreDao,
                         @Qualifier("DbUserService") UserService userService) {
        this.filmStorage = filmStorage;
        this.genreDao = genreDao;
        this.userService = userService;
    }

    @Override
    public List<Film> getFilms() {
        return filmStorage.getFilms().stream()
                .peek(film -> genreDao.getGenresByFilm(film.getId())
                        .forEach(film::addGenre))
                .collect(Collectors.toList());
    }

    @Override
    public Film getFilm(int id) {
        Film film = filmStorage.getFilm(id);
        genreDao.getGenresByFilm(film.getId())
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
        return filmStorage.getPopularFilms(count).stream()
                .peek(film -> genreDao.getGenresByFilm(film.getId())
                        .forEach(film::addGenre))
                .collect(Collectors.toList());
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