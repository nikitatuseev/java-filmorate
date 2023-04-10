package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.film.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class FilmServiceTest {

    @Mock
    private InMemoryFilmStorage filmStorage;

    @Mock
    private InMemoryUserStorage userStorage;

    private FilmService filmService;

    private final Set<Integer> emptySet = new HashSet<>();

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        filmService = new FilmService(filmStorage, userStorage);
    }

    @Test
    void testGetFilms() {
        List<Film> films = new ArrayList<>();
        films.add(new Film(1, "film", "newFilm", LocalDate.now(), 90, emptySet));
        films.add(new Film(2, "film", "newFilm", LocalDate.now(), 90, emptySet));
        when(filmStorage.getFilms()).thenReturn(films);

        List<Film> result = filmService.getFilms();

        Assertions.assertEquals(films, result);
    }

    @Test
    void testCreate() {
        Film film = new Film(1, "film", "newFilm", LocalDate.now(), 90, emptySet);
        when(filmStorage.addFilm(film)).thenReturn(film);

        Film result = filmService.create(film);

        Assertions.assertEquals(film, result);
    }

    @Test
    void testUpdate() {
        Film film = new Film(1, "film", "newFilm", LocalDate.now(), 90, emptySet);
        when(filmStorage.updateFilm(film)).thenReturn(film);

        Film result = filmService.update(film);

        Assertions.assertEquals(film, result);
    }

    @Test
    void testGetFilm() {
        Film film = new Film(1, "film", "newFilm", LocalDate.now(), 90, emptySet);
        when(filmStorage.getFilm(1)).thenReturn(film);

        Film result = filmService.getFilm(1);

        Assertions.assertEquals(film, result);
    }

    @Test
    void testAddLike() {
        Film film = new Film(1, "film", "newFilm", LocalDate.now(), 90, emptySet);
        User user = new User(1, "dd@mail.ru", "dc", "user1", LocalDate.of(1990, 2, 1),
                emptySet);
        when(filmStorage.getFilm(1)).thenReturn(film);
        when(userStorage.getUser(1)).thenReturn(user);

        filmService.addLike(1, 1);

        assertTrue(film.getLikes().contains(1));
    }

    @Test
    void testRemoveLike() {
        Film film = new Film(1, "film", "newFilm", LocalDate.now(), 90, emptySet);
        User user = new User(1, "dd@mail.ru", "dc", "user1", LocalDate.of(1990, 2, 1),
                emptySet);
        when(filmStorage.getFilm(1)).thenReturn(film);
        when(userStorage.getUser(1)).thenReturn(user);

        film.getLikes().add(1);

        filmService.removeLike(1, 1);

        assertFalse(film.getLikes().contains(1));
    }

    @Test
    void testGetPopularFilms() {
        List<Film> films = new ArrayList<>();
        films.add(new Film(1, "film1", "newFilm1", LocalDate.now(), 90, emptySet));
        films.add(new Film(2, "film2", "newFilm2", LocalDate.now(), 90, emptySet));
        films.add(new Film(3, "film3", "newFilm3", LocalDate.now(), 90, emptySet));

        films.get(0).getLikes().add(1);
        films.get(0).getLikes().add(2);
        films.get(1).getLikes().add(1);
        films.get(1).getLikes().add(2);
        films.get(1).getLikes().add(3);
        films.get(2).getLikes().add(1);

        when(filmStorage.getFilms()).thenReturn(films);

        List<Film> result = filmService.getPopularFilms(2);

        assertEquals(2, result.size());
        assertEquals(films.get(0), result.get(0));
        assertEquals(films.get(1), result.get(1));
    }
}

