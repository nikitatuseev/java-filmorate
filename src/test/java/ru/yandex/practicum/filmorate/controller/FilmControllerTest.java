package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.FilmException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

public class FilmControllerTest {

    FilmController controller = new FilmController();

    @Test
    void addFilm() {
        Film film = new Film(1, "film", "newFilm", LocalDate.now(), 90);
        controller.addFilm(film);
        assertEquals(film, controller.getFilms().get(0));
    }

    @Test
    void update() {
        Film film = new Film(1, "film", "newFilm", LocalDate.now(), 90);
        controller.addFilm(film);
        Film updateFilm = new Film(1, "film", "Film", LocalDate.now(), 150);
        controller.updateFilm(updateFilm);
        assertEquals(updateFilm, controller.getFilms().get(0));
    }

    @Test
    public void normalReleaseDate() {
        Film film = new Film(1, "film", "newFilm", LocalDate.now(), 90);
        assertDoesNotThrow(() -> controller.checkDate(film));
    }

    @Test
    public void wrongReleaseDate() {
        Film film = new Film(1, "film", "newFilm",
                LocalDate.of(1895, 11, 28), 90);
        assertThrows(FilmException.class, () -> controller.checkDate(film));
    }
}
