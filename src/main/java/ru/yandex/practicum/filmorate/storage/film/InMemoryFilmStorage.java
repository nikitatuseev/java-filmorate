package ru.yandex.practicum.filmorate.storage.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.FilmException;
import ru.yandex.practicum.filmorate.exception.IncorrectParameterException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.*;

@Slf4j
@Component
public class InMemoryFilmStorage implements FilmStorage {
    private int idGenerator = 1;
    private static final LocalDate FILM_BIRTHDAY = LocalDate.of(1895, 12, 28);
    private final Map<Integer, Film> films = new HashMap<>();

    @Override
    public List<Film> getFilms() {
        return new ArrayList<>(films.values());
    }

    @Override
    public Film addFilm(Film film) {
        if (films.values().stream().noneMatch(u -> u.getDescription().equals(film.getDescription()))) {
            checkDate(film);
            film.setId(idGenerator++);
            films.put(film.getId(), film);
            log.info("фильм {} добавлен", film);
        }
        return film;
    }

    @Override
    public Film updateFilm(Film film) {
        checkDate(film);
        if (films.containsKey(film.getId())) {
            films.put(film.getId(), film);
            return film;
        } else {
            log.error("Фильм с id = {} не найден", film.getId());
            throw new FilmException("Фильм с таким id не существует");
        }
    }

    @Override
    public Film getFilm(int id) {
        if (!films.containsKey(id)) {
            log.error("Фильм с id = {} не найден", id);
            throw new FilmException("Фильм с таким id не существует");
        }
        return films.get(id);
    }

    protected void checkDate(Film film) {
        if (film.getReleaseDate().isBefore(FILM_BIRTHDAY)) {
            log.error("дата релиза не может быть раньше 28 декабря 1895 года.");
            throw new IncorrectParameterException("releaseDate");
        }
    }
}
