CREATE TABLE IF NOT EXISTS USERS
(
    USER_ID  INTEGER auto_increment
        primary key,
    EMAIL    CHARACTER VARYING(255) not null,
    LOGIN    CHARACTER VARYING(50)  not null,
    NAME     CHARACTER VARYING(50)  not null,
    BIRTHDAY DATE                   not null
);

CREATE TABLE IF NOT EXISTS MPA
(
    MPA_ID      INTEGER auto_increment
        primary key,
    NAME        CHARACTER VARYING(50) not null UNIQUE,
    DESCRIPTION CHARACTER VARYING(100)
);

CREATE TABLE IF NOT EXISTS GENRE
(
    GENRE_ID INTEGER auto_increment
        primary key,
    NAME     CHARACTER VARYING(50) UNIQUE
);

CREATE TABLE IF NOT EXISTS FILMS
(
    FILM_ID      INTEGER auto_increment
        primary key,
    NAME         CHARACTER VARYING(150) not null,
    DESCRIPTION  CHARACTER VARYING(200) not null,
    RELEASE_DATE DATE                   not null,
    DURATION     INTEGER                not null,
    MPA_ID       INTEGER                not null
        references MPA (MPA_ID) ON DELETE RESTRICT
);

CREATE TABLE IF NOT EXISTS FRIENDS
(
    USER_ID   INTEGER not null
        references USERS (USER_ID) ON DELETE CASCADE,
    FRIEND_ID INTEGER not null
        references USERS (USER_ID) ON DELETE CASCADE,
    STATUS    BOOLEAN not null,
    primary key (USER_ID, FRIEND_ID)
);

CREATE TABLE IF NOT EXISTS FILM_GENRE
(
    FILM_ID  INTEGER not null
        references FILMS ON DELETE CASCADE,
    GENRE_ID INTEGER not null
        references GENRE ON DELETE CASCADE,
    primary key (FILM_ID, GENRE_ID)
);

CREATE TABLE IF NOT EXISTS LIKES
(
    FILM_ID INTEGER not null
        references FILMS ON DELETE CASCADE,
    USER_ID INTEGER not null
        references USERS ON DELETE CASCADE,
    primary key (FILM_ID, USER_ID)
);
