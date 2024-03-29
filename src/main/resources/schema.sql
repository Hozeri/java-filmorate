DROP TABLE IF EXISTS users, friends, mpa, films, likes, film_genres, genres;

CREATE TABLE users (
  id INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY NOT NULL,
  login varchar(255) UNIQUE NOT NULL,
  email varchar(255) UNIQUE NOT NULL,
  name varchar(255),
  birthday date
);

CREATE TABLE friends (
  user_id integer REFERENCES users (id) ON DELETE CASCADE,
  friend_id integer REFERENCES users (id) ON DELETE CASCADE,
  PRIMARY KEY (user_id, friend_id)
);

CREATE TABLE mpa (
  id INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
  name varchar(255)
);

CREATE TABLE genres (
  id INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
  name varchar(255)
);

CREATE TABLE films (
  id INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY NOT NULL,
  name varchar(255) NOT NULL,
  description varchar(200),
  release_date date,
  duration integer,
  mpa_id integer REFERENCES mpa (id)
);

CREATE TABLE likes (
  film_id integer REFERENCES films (id) ON DELETE CASCADE,
  user_id integer REFERENCES users (id) ON DELETE CASCADE,
  PRIMARY KEY (film_id, user_id)
);

CREATE TABLE film_genres (
  film_id integer REFERENCES films (id) ON DELETE CASCADE,
  genre_id integer REFERENCES genres (id) ON DELETE CASCADE,
  PRIMARY KEY (film_id, genre_id)
);