package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Data
@Builder
public class User {
    @Null(groups = CreateGroup.class)
    @NotNull(groups = UpdateGroup.class)
    private int id;
    @NotBlank(message = "Email не может быть пустым")
    @Email(message = "Введен некорректный email")
    private String email;
    @NotBlank(message = "Логин не может быть пустым")
    @Pattern(regexp = "\\S+", message = "Логин содержит пробелы")
    private String login;
    private String name;
    @NotNull(message = "Дата рождения не может быть пустой")
    private LocalDate birthday;
    private final Set<Integer> friends = new HashSet<>();

    public void addFriend(int id) {
        friends.add(id);
    }

    public boolean deleteFriend(int id) {
        return friends.remove(id);
    }

    public Map<String, Object> toMap() {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("email", email);
        parameters.put("login", login);
        parameters.put("name", name);
        parameters.put("birthday", birthday);
        return parameters;
    }
}