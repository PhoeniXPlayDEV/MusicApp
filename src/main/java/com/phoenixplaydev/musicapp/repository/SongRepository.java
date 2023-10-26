package com.phoenixplaydev.musicapp.repository;

import com.phoenixplaydev.musicapp.model.Tables;
import com.phoenixplaydev.musicapp.model.enums.Genre;
import com.phoenixplaydev.musicapp.model.tables.AuthorToSong;
import com.phoenixplaydev.musicapp.model.tables.pojos.Song;
import org.jooq.DSLContext;
import org.jooq.exception.DataAccessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class SongRepository {

    @Autowired
    private DSLContext dslContext;

    public boolean isSongNotExists(Long songID) {
        return !dslContext.selectFrom(Tables.SONG)
                .where(Tables.SONG.ID.eq(songID))
                .fetch().isNotEmpty();
    }

    public Song getSongByID(Long songID) {
        return dslContext.selectFrom(Tables.SONG)
                .where(Tables.SONG.ID.eq(songID))
                .fetchOneInto(Song.class);
    }

    public List<Song> getAllSongs() {
        return dslContext.selectFrom(Tables.SONG)
                .fetchInto(Song.class);
    }

    public void addSong(Song song) {
        dslContext.insertInto(
                Tables.SONG,
                Tables.SONG.NAME,
                Tables.SONG.COVER,
                Tables.SONG.LENGTH,
                Tables.SONG.GENRE,
                Tables.SONG.FILE,
                Tables.SONG.ALBUM_ID
        ).values(
                song.getName(),
                song.getCover(),
                song.getLength(),
                song.getGenre(),
                song.getFile(),
                song.getAlbumId()
        ).execute();
    }

    public void updateSongInfo(Song song) {
        dslContext.update(Tables.SONG)
                .set(dslContext.newRecord(Tables.SONG, song))
                .where(Tables.SONG.ID.eq(song.getId()))
                .returning()
                .fetchOptional()
                .orElseThrow(() ->
                        new DataAccessException(
                                "Can't update song with ID = " +
                                        song.getId()))
                .into(Song.class);
    }

    public void deleteSong(Long songID) {
        dslContext.deleteFrom(Tables.SONG)
                .where(Tables.SONG.ID.eq(songID)).execute();
    }

    public List<Song> getSongsByGenre(Genre genre) {
        return dslContext.selectFrom(Tables.SONG)
                .where(Tables.SONG.GENRE.eq(genre))
                .fetchInto(Song.class);
    }

}
