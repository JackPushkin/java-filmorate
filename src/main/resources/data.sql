DELETE FROM likes;
DELETE FROM friends;
DELETE FROM films_genres;
DELETE FROM users;
DELETE FROM films;
DELETE FROM mpa_rating;
DELETE FROM genres;

INSERT INTO users (EMAIL, LOGIN, NAME, BIRTHDAY)
VALUES
('jack@mail.ru', 'jackson', 'Jack', '1995-03-14'),
('john@mail.ru', 'johnson', 'John', '1992-03-14'),
('johns@mail.ru', 'johnson', 'John', '1992-03-14');

INSERT INTO friends (ID_USER, ID_FRIEND, FRIENDSHIP_STATUS)
VALUES
(1, 2, TRUE),
(2, 1, TRUE);

INSERT INTO mpa_rating (RATING_NAME)
VALUES
('G'),
('PG'),
('PG-13'),
('R'),
('NC-17');

INSERT INTO films (TITLE, DESCRIPTION, RELEASE_DATE, DURATION, ID_RATING)
VALUES
('Film1', 'Desc1', '1990-01-01', 120, 3),
('Film2', 'Desc2', '1995-01-01', 110, 2),
('Film3', 'Desc3', '2012-03-05', 95, 4);

INSERT INTO likes (ID_USER, ID_FILM)
VALUES
(1, 2),
(2, 3),
(1, 1);

INSERT INTO genres (GENRE_NAME)
VALUES
('Комедия'),
('Драма'),
('Мультфильм'),
('Триллер'),
('Документальный'),
('Боевик');

INSERT INTO films_genres (ID_FILM, ID_GENRE)
VALUES
(1, 1),
(1, 2),
(1, 3),
(2, 5),
(2, 6),
(3, 5),
(3, 4);