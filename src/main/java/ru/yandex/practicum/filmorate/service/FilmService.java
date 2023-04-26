package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.film.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class FilmService {
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

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
        Film film = filmStorage.getFilm(filmId);
        userStorage.getUser(userId);
        film.addLike(userId);
        log.info("Пользователь с id " + userId + " Поставил лайк фильму с id " + filmId);
        return filmStorage.updateFilm(film);
    }

    public Film removeLike(int filmId, int userId) {
        Film film = filmStorage.getFilm(filmId);
        userStorage.getUser(userId);
        film.deleteLike(userId);
        log.info("Пользователь с id " + userId + " удалил лайк у фильма с id " + filmId);
        return filmStorage.updateFilm(film);
    }

    public List<Film> getPopularFilms(int count) {
        List<Film> popularFilms = new ArrayList<>(filmStorage.getFilms());
        return popularFilms.stream()
                .sorted(((o1, o2) -> Integer.compare(o2.getLikes().size(), o1.getLikes().size())))
                .limit(count)
                .collect(Collectors.toList());
    }
}
