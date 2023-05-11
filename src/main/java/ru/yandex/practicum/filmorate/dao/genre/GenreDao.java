package ru.yandex.practicum.filmorate.dao.genre;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;

public interface GenreDao {
    List<Genre> getGenres();

    Genre getGenre(int id);

    List<Genre> getAllGenres(List<Film> films);

    List<Genre> getGenresByFilm(int id);
}
