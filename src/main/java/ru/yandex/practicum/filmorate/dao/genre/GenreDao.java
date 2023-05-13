package ru.yandex.practicum.filmorate.dao.genre;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;
import java.util.Map;

public interface GenreDao {
    List<Genre> getGenres();

    Genre getGenre(int id);

    Map<Integer, List<Genre>> getAllGenres(List<Film> films);

    List<Genre> getGenresByFilm(int id);
}
