package ru.yandex.practicum.filmorate.service.film;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmService {

    List<Film> getFilms();

    Film getFilm(int id);

    Film addFilm(Film film);

    Film updateFilm(Film film);

    List<Film> getPopularFilms(Integer count);

    List<Integer> addLike(int filmId, int userId);

    List<Integer> deleteLike(int filmId, int userId);
}
