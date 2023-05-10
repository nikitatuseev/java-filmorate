package ru.yandex.practicum.filmorate.dao.genre;

import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;
import java.util.Map;

public interface GenreDao {
    List<Genre> getGenres();

    Genre getGenre(int id);
    List<Genre> getAllGenres();
    List<Genre> getGenresByFilm(int id);
}
