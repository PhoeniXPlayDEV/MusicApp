SET search_path TO music_app;

INSERT INTO music_app.user (role, nickname, email, password)
    VALUES ('User', 'Austin', 'user1@outlook.com', '$2a$10$vBdILaj/gxRbuPOhQLZH6.a1eR16KPU4TDZjwF1x8TTKa7RtsraEm'); -- password: qwerty1

INSERT INTO music_app.user (role, nickname, email, password)
    VALUES ('User', 'Jack', 'jack@gmail.com', '$2a$10$rP6x0uUErYfYuBUx.yNWiOc5cwK45MPUFQGc/sP97zijV6Kr/NKyO'); -- password: qwerty2

INSERT INTO music_app.user (role, nickname, email, password)
    VALUES ('Admin', 'Alan', 'alan@yahoo.com', '$2a$10$ZOVQvR61Ka/HeABHasB2rOibOUgP.2x51/zB3JbKimDJWlo8xPF.u'); -- password: qwerty3