SET search_path TO music_app;

INSERT INTO music_app.user (role, nickname, email, password) VALUES
    ('User', 'Austin', 'austin@outlook.com', '$2a$10$vBdILaj/gxRbuPOhQLZH6.a1eR16KPU4TDZjwF1x8TTKa7RtsraEm'), -- password: qwerty1
    ('User', 'Jack', 'jack@gmail.com', '$2a$10$rP6x0uUErYfYuBUx.yNWiOc5cwK45MPUFQGc/sP97zijV6Kr/NKyO'), -- password: qwerty2
    ('Admin', 'Alan', 'alan@yahoo.com', '$2a$10$ZOVQvR61Ka/HeABHasB2rOibOUgP.2x51/zB3JbKimDJWlo8xPF.u'); -- password: qwerty3

INSERT INTO album (name) VALUES
    ('Поп-чилаут'),
    ('Минимал-хаус'),
    ('Азия');

INSERT INTO song (name, cover, length, genre, file, album_id) VALUES
    ('Move On', null, 180.5, 'Pop', '\\x0123456789ABCDEF', 1),
    ('I Won''t', null, 215.2, 'Pop', '\\x23456789ABCDEF0123', 1),
    ('Godless', null, 179.3, 'Pop', '\\x89ABCDEF0123456789', 1),
    ('Herb', null, 423.1, 'Electronic', '\\x8ABEDF345F6789', 2),
    ('Aestivation', null, 464.7, 'Electronic', '\\x123AF3D45FF6789', 2),
    ('Japanese Spring', null, 183.4, 'HipHop', '\\x123AF3D45FF6789', 3);

INSERT INTO singer (name, cover) VALUES
    ('Ava August', null),
    ('Etham', null),
    ('Luca George', null),
    ('PTTY', null),
    ('Lawrence', null),
    ('Vindu', null);

INSERT INTO music_band (name, cover) VALUES
    ('Band 1', null),
    ('Band 2', null),
    ('Band 3', null),
    ('Band 4', null),
    ('Band 5', null);

INSERT INTO author_to_song (song_id, singer_id, music_band_id) VALUES
    (1, 1, 1),
    (2, 2, 1),
    (3, 3, 2),
    (4, 4, 3),
    (5, 5, 4),
    (6, 6, 5);

INSERT INTO liked_song (user_id, song_id) VALUES
    (1, 2),
    (1, 5),
    (1, 3),
    (2, 3),
    (2, 4),
    (3, 1),
    (3, 2),
    (3, 6);