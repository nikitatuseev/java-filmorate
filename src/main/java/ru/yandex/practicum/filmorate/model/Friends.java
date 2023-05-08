package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Friends {
    private int userId;
    private int friendId;
    private boolean status;
}