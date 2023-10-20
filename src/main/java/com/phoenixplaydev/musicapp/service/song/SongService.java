package com.phoenixplaydev.musicapp.service.song;

import com.phoenixplaydev.musicapp.model.Tables;
import com.phoenixplaydev.musicapp.model.tables.pojos.Song;
import org.jooq.DSLContext;
import org.jooq.exception.DataAccessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SongService implements ISongService {

    @Autowired
    private DSLContext dslContext;

    @Override
    public Song getSongByID(Long id) {
        return dslContext.selectFrom(Tables.SONG)
                .where(Tables.SONG.ID.eq(id))
                .fetchOptional().orElseThrow(() ->
                        new DataAccessException(
                            "Can't find song with ID = " + id))
                .into(Song.class);
    }

    @Override
    public List<Song> getAllSongs() {
        return dslContext.selectFrom(Tables.SONG)
                .fetchInto(Song.class);
    }

    @Override
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

    @Override
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

    @Override
    public void setAuthor(Long songID, Long singerID, Long bandID) {
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

    @Override
    public void deleteSong(Long songID) {
        dslContext.deleteFrom(Tables.SONG)
                .where(Tables.SONG.ID.eq(songID)).execute();
        //TODO: Check that info about song will be deleted
    }
}
