package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.FilmException;
import ru.yandex.practicum.filmorate.exception.UserException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.film.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
public class FilmService {
    FilmStorage filmStorage;
    UserStorage userStorage;

    @Autowired
    public FilmService(InMemoryFilmStorage filmStorage, InMemoryUserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    public List<Film> getFilms() {
        return filmStorage.getFilms();
    }

    public Film create(Film film) {
        return filmStorage.addFilm(film);
    }

    public Film update(Film film) {
        return filmStorage.updateFilm(film);
    }

    public Film getFilm(int id) {
        return filmStorage.getFilm(id);
    }

    public Film addLike(int filmId, int userId) {
        if (filmStorage.getFilm(filmId) == null) {
            log.error("Фильма с id {} не существует ", filmId);
            throw new FilmException("Фильма с id" + filmId + "не существует ");
        }
        if (userStorage.getUser(userId) == null) {
            log.error("Пользователя с id {} не существует ", filmId);
            throw new UserException("Пользователя с id" + filmId + "не существует ");
        }
        Film film = filmStorage.getFilm(filmId);
        Set<Integer> likes = film.getLikes();
        likes.add(userId);
        film.setLikes(likes);
        log.info("Пользователь с id " + userId + " Поставил лайк фильму с id " + filmId);
        return filmStorage.updateFilm(film);
    }

    public Film removeLike(int filmId, int userId) {
        if (filmStorage.getFilm(filmId) == null) {
            log.error("Фильма с id {} не существует ", filmId);
            throw new FilmException("Фильма с id" + filmId + "не существует ");
        }
        if (userStorage.getUser(userId) == null) {
            log.error("Пользователя с id {} не существует ", filmId);
            throw new UserException("Пользователя с id" + filmId + "не существует ");
        }
        Film film = filmStorage.getFilm(filmId);
        Set<Integer> likes = film.getLikes();
        likes.remove(userId);
        film.setLikes(likes);
        log.info("Пользователь с id " + userId + " удалил лайк у фильма с id " + filmId);
        return filmStorage.updateFilm(film);
    }

    public List<Film> getPopularFilms(int count) {
        List<Film> popularFilms = new ArrayList<>(filmStorage.getFilms());
        if (count > popularFilms.size()) {
            count = popularFilms.size();
        }
        return popularFilms.stream()
                .sorted(((o1, o2) -> Integer.compare(o2.getLikes().size(), o1.getLikes().size())))
                .limit(count)
                .collect(Collectors.toList());
    }
}
