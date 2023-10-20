create schema music_app;

set search_path to music_app;

create type user_role as enum ('Admin', 'User');

create type genre as enum ('Blues', 'Country', 'Electronic', 'HipHop', 'Jazz', 'Pop', 'RnB', 'Rock','Metal', 'Punk');

create table music_app.user
(
    id       bigserial primary key,
    role     user_role         not null default 'User'::user_role,
    nickname character varying not null,
    email    character varying not null,
    password character varying not null
);

create table singer
(
    id    bigserial primary key,
    name  character varying not null,
    cover bytea
);

create table music_band
(
    id    bigserial primary key,
    name  character varying not null,
    cover bytea
);

create table album
(
    id    bigserial primary key,
    name  character varying not null,
    cover bytea
);

create table author_to_album
(
    album_id      bigint not null references album (id) on delete cascade on update cascade,
    singer_id     bigint references singer (id) on delete cascade on update cascade,
    music_band_id bigint references music_band (id) on delete cascade on update cascade
);

alter table author_to_album
    add constraint author_to_album_pk primary key (album_id, singer_id, music_band_id);

alter table author_to_album
    add constraint author_to_album_check check ( not (singer_id is null and music_band_id is null));

create table song
(
    id       bigserial primary key,
    name     character varying not null,
    cover    bytea,
    length   double precision  not null,

    genre    genre             not null,
    file     bytea             not null,
    album_id bigint            not null references album (id) on delete cascade on update cascade
);


create table author_to_song
(
    song_id       bigint references song (id) on delete cascade on update cascade,
    singer_id     bigint references singer (id) on delete cascade on update cascade,
    music_band_id bigint references music_band (id) on delete cascade on update cascade
);

alter table author_to_song
    add constraint author_to_song_pk primary key (song_id, singer_id, music_band_id);

alter table author_to_song
    add constraint author_to_song_check check ( not (singer_id is null and music_band_id is null) );

create table liked_song
(
    user_id bigint not null references music_app.user (id) on delete cascade on update cascade,
    song_id bigint not null references song (id) on delete cascade on update cascade
);

alter table liked_song
    add constraint liked_song_pk primary key (user_id, song_id);


create table liked_band
(
    user_id bigint not null references music_app.user (id) on delete cascade on update cascade,
    band_id bigint not null references music_band (id) on delete cascade on update cascade
);

alter table liked_band
    add constraint liked_band_pk primary key (user_id, band_id);

create table liked_album
(
    user_id  bigint not null references music_app.user (id) on delete cascade on update cascade,
    album_id bigint not null references album (id) on delete cascade on update cascade
);

alter table liked_album
    add constraint liked_album_pk primary key (user_id, album_id);

create table liked_singer
(
    user_id   bigint not null references music_app.user (id) on delete cascade on update cascade,
    singer_id bigint not null references singer (id) on delete cascade on update cascade
);

alter table liked_singer
    add constraint liked_singer_pk primary key (user_id, singer_id);