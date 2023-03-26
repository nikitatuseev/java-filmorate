package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.FilmException;
import ru.yandex.practicum.filmorate.exception.UserException;
import ru.yandex.practicum.filmorate.model.CreateGroup;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.UpdateGroup;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {
    private int idGenerator = 1;
    private final LocalDate filmBirthday = LocalDate.of(1895, 12, 28);
    private final Map<Integer, Film> films = new HashMap<>();

    @GetMapping()
    public List<Film> getFilms() {
        return new ArrayList<>(films.values());
    }

    @PostMapping()
    public Film addFilm(@RequestBody @Validated(CreateGroup.class) Film film) {
        if (films.values().stream().noneMatch(u -> u.getDescription().equals(film.getDescription()))) {
            if (film.getReleaseDate().isBefore(filmBirthday)) {
                log.error("дата релиза не может быть раньше 28 декабря 1895 года.");
                throw new FilmException("дата релиза не может быть раньше 28 декабря 1895 года.");
            }
            film.setId(idGenerator++);
            films.put(film.getId(), film);
            log.info("фильм добавлен");
            return film;
        } else {
            log.error("Фильм с таким описанием уже есть");
            throw new UserException("Такой фильм уже существует");
        }
    }

    @PutMapping
    public Film updateFilm(@RequestBody @Validated(UpdateGroup.class) Film film) {
        if (film.getReleaseDate().isBefore(filmBirthday)) {
            log.error("дата релиза не может быть раньше 28 декабря 1895 года.");
            throw new FilmException("дата релиза не может быть раньше 28 декабря 1895 года.");
        }
        if (films.containsKey(film.getId())) {
            films.put(film.getId(), film);
            return film;
        } else {
            log.error("Фильм с id = {} не найден", film.getId());
            throw new RuntimeException("Фильм с таким id не существует");
        }
    }
}
