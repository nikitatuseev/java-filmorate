package ru.yandex.practicum.filmorate.service.film;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.genre.GenreDao;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.service.user.UserService;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.util.*;

@Service("DbFilmService")
public class DbFilmService implements FilmService {
    private final FilmStorage filmStorage;
    private final UserService userService;
    private final GenreDao genreDao;

    @Autowired
    public DbFilmService(FilmStorage filmStorage,
                         @Qualifier("DbUserService") UserService userService, GenreDao genreDao) {
        this.filmStorage = filmStorage;
        this.userService = userService;
        this.genreDao = genreDao;
    }

    //это все, что я смог придумать без использования запросов в цикле, потому что я не понимаю,
    // как сделать все только в методе getAllGenres. ведь после
    // его выполнения получается два списка: список фильмов и список их жанров,
    // но связи между ними нет, чтобы можно было их сразу связать и добавить каждому фильму только его жанр.
    @Override
    public List<Film> getFilms() {
        List<Film> films = filmStorage.getFilms();
        return genresByFilmId(films);
    }

    public List<Film> genresByFilmId(List<Film> films) {
        Map<Integer, List<Genre>> genresByFilmId = genreDao.getAllGenres(films);
        for (Film film : films) {
            List<Genre> genres = genresByFilmId.getOrDefault(film.getId(), new ArrayList<>());
            film.setGenres(genres);
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
        List<Film> popularFilms = filmStorage.getPopularFilms(count);
        return genresByFilmId(popularFilms);
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