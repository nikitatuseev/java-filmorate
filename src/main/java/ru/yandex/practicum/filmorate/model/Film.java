package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Data
@Builder
public class Film {
    @Null(groups = CreateGroup.class)
    @NotNull(groups = UpdateGroup.class)
    private int id;
    @NotBlank(message = "нет названия")
    private String name;
    @Size(min = 1, max = 200, message = "Длина описания должна быть от 1 до 200 символов")
    private String description;
    @NotNull(message = "нет даты релиза")
    private LocalDate releaseDate;
    @Positive
    private int duration;
    private Mpa mpa;
    private final Set<Genre> genres = new HashSet<>();
    private final Set<Integer> likes = new HashSet<>();

    public void addLike(int id) {
        likes.add(id);
    }

    public boolean deleteLike(int id) {
        return likes.remove(id);
    }

    public List<Genre> getGenres() {
        return genres.stream().sorted(Comparator.comparingInt(Genre::getId)).collect(Collectors.toList());
    }

    public void addGenre(Genre genre) {
        genres.add(genre);
    }

    public boolean deleteGenre(Genre genre) {
        return genres.remove(genre);
    }

    public Map<String, Object> toMap() {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("name", name);
        parameters.put("description", description);
        parameters.put("release_date", releaseDate);
        parameters.put("duration", duration);
        parameters.put("mpa_id", mpa.getId());
        return parameters;
    }
}
