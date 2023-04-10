package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.Set;

@Data
@AllArgsConstructor
public class User {
    @Null(groups = CreateGroup.class)
    @NotNull(groups = UpdateGroup.class)
    private Integer id;
    @Email(groups = {CreateGroup.class, UpdateGroup.class})
    private String email;
    @NotBlank(groups = {CreateGroup.class, UpdateGroup.class})
    @Pattern(regexp = "^\\S+$", groups = {CreateGroup.class, UpdateGroup.class})
    private String login;
    private String name;
    @NotNull(groups = {CreateGroup.class, UpdateGroup.class})
    private LocalDate birthday;
    private Set<Integer> friends;

    public boolean addFriend(User user) {
        return friends.add(user.getId());
    }

    public boolean deleteFriend(User user) {
        return friends.remove(user.getId());
    }
}