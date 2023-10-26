package com.phoenixplaydev.musicapp.repository;

import com.phoenixplaydev.musicapp.model.Tables;
import com.phoenixplaydev.musicapp.model.tables.pojos.AuthorToSong;
import com.phoenixplaydev.musicapp.model.tables.pojos.Song;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class AuthorToSongRepository {

    @Autowired
    private DSLContext dslContext;

    public List<AuthorToSong> getAllSongAuthors(Long songID) {
        return dslContext.selectFrom(Tables.AUTHOR_TO_SONG)
                .where(Tables.AUTHOR_TO_SONG.SONG_ID.eq(songID))
                .fetchInto(AuthorToSong.class);
    }

    public void addSongAuthor(Long songID, Long singerID, Long bandID) {
        dslContext.insertInto(
                Tables.AUTHOR_TO_SONG,
                Tables.AUTHOR_TO_SONG.SONG_ID,
                Tables.AUTHOR_TO_SONG.SINGER_ID,
                Tables.AUTHOR_TO_SONG.MUSIC_BAND_ID
        ).values(
                songID,
                singerID,
                bandID
        ).execute();
    }

    public void deleteSongAuthor(Long songID, Long singerID, Long bandID) {
        dslContext.deleteFrom(Tables.AUTHOR_TO_SONG)
                .where(Tables.AUTHOR_TO_SONG.SONG_ID.eq(songID)
                        .and(Tables.AUTHOR_TO_SONG.SINGER_ID.eq(singerID))
                        .and(Tables.AUTHOR_TO_SONG.MUSIC_BAND_ID.eq(bandID))
                ).execute();
    }

    public List<Song> getSingerSongs(Long singerID) {
        return dslContext.selectFrom(Tables.AUTHOR_TO_SONG)
                .where(Tables.AUTHOR_TO_SONG.SINGER_ID.eq(singerID))
                .fetchInto(Song.class);
    }

    public List<Song> getMusicBandSongs(Long bandID) {
        return dslContext.selectFrom(Tables.AUTHOR_TO_SONG)
                .where(Tables.AUTHOR_TO_SONG.MUSIC_BAND_ID.eq(bandID))
                .fetchInto(Song.class);
    }

}
