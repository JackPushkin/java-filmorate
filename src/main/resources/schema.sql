DROP TABLE IF EXISTS friends;
DROP TABLE IF EXISTS likes;
DROP TABLE IF EXISTS users;
DROP TABLE IF EXISTS films_genres;
DROP TABLE IF EXISTS genres;
DROP TABLE IF EXISTS films;
DROP TABLE IF EXISTS mpa_rating;

CREATE TABLE IF NOT EXISTS users (
	id_user serial PRIMARY KEY,
	email varchar(255) NOT NULL,
	login varchar(255) NOT NULL,
	name varchar(255) NOT NULL,
	birthday date NOT NULL,
	UNIQUE (email)
);

CREATE TABLE IF NOT EXISTS friends (
	id serial PRIMARY KEY,
	id_user integer REFERENCES users (id_user),
	id_friend integer REFERENCES users (id_user),
	friendship_status boolean NOT NULL,
	UNIQUE (id_user, id_friend)
);

CREATE TABLE IF NOT EXISTS mpa_rating (
	id_rating serial PRIMARY KEY,
	rating_name varchar(10) NOT NULL
);

CREATE TABLE IF NOT EXISTS films (
	id_film serial PRIMARY KEY,
	title varchar(255) NOT NULL,
	description varchar NOT NULL,
	release_date date NOT NULL,
	duration integer NOT NULL,
	id_rating integer REFERENCES mpa_rating (id_rating)
);

CREATE TABLE IF NOT EXISTS likes (
	id serial PRIMARY KEY,
	id_user integer REFERENCES users (id_user),
	id_film integer REFERENCES films (id_film),
	UNIQUE (id_user, id_film)
);

CREATE TABLE IF NOT EXISTS genres (
	id_genre serial PRIMARY KEY,
	genre_name varchar(32) NOT NULL
);

CREATE TABLE IF NOT EXISTS films_genres (
	id serial PRIMARY KEY,
	id_film integer REFERENCES films (id_film),
	id_genre integer REFERENCES genres (id_genre),
	UNIQUE (id_film, id_genre)
);