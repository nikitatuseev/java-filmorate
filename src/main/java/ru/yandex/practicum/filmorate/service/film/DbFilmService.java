package ru.yandex.practicum.filmorate.service.film;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.FilmDao;
import ru.yandex.practicum.filmorate.dao.genre.GenreDao;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.service.user.UserService;

import java.util.List;

@Service("DbFilmService")
public class DbFilmService implements FilmService {
    private final FilmDao filmStorage;
    private final UserService userService;
    private final GenreDao genreDao;

    @Autowired
    public DbFilmService(FilmDao filmStorage,
                         @Qualifier("DbUserService") UserService userService, GenreDao genreDao) {
        this.filmStorage = filmStorage;
        this.userService = userService;
        this.genreDao = genreDao;
    }

    @Override
    public List<Film> getFilms() {
        List<Film> films = filmStorage.getFilms();
        for (Film film : films) {
            List<Genre> filmGenre = genreDao.getGenresByFilm(film.getId());
            for (Genre genre : filmGenre) {
                film.addGenre(genre);
            }
        }
        return films;
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
        // Получаем список популярных фильмов из filmStorage
        List<Film> popularFilms = filmStorage.getPopularFilms(count);

        // Для каждого фильма добавляем соответствующие жанры
        for (Film film : popularFilms) {
            List<Genre> filmGenres = genreDao.getGenresByFilm(film.getId());
            for (Genre genre : filmGenres) {
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