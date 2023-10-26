package com.phoenixplaydev.musicapp.repository;

import com.phoenixplaydev.musicapp.model.Tables;
import com.phoenixplaydev.musicapp.model.enums.Genre;
import com.phoenixplaydev.musicapp.model.tables.pojos.Song;
import org.jooq.DSLContext;
import org.jooq.exception.DataAccessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class LikedSongsRepository {

    @Autowired
    private DSLContext dslContext;

    public List<Song> getLikedSongs(Long userID) {
        return dslContext.select()
                .from(Tables.LIKED_SONG)
                .join(Tables.SONG)
                .on(Tables.LIKED_SONG.SONG_ID.eq(Tables.SONG.ID))
                .where(Tables.LIKED_SONG.USER_ID.eq(userID))
                .fetchInto(Song.class);
    }

    public boolean isSongLiked(Long userID, Long songID) {
        return dslContext.selectFrom(Tables.LIKED_SONG)
                .where(Tables.LIKED_SONG.USER_ID.eq(userID).and(Tables.LIKED_SONG.SONG_ID.eq(songID)))
                .fetch().isNotEmpty();
    }

    public void likeSong(Long userID, Long songID) {
        if(isSongLiked(userID, songID))
            throw new DataAccessException("Song with ID = " + songID +
                    " already liked by the user with ID = " + userID + "!");
        dslContext.insertInto(
                Tables.LIKED_SONG,
                Tables.LIKED_SONG.USER_ID,
                Tables.LIKED_SONG.SONG_ID
        ).values(
                userID,
                songID
        ).execute();
    }

    public void unlikeSong(Long userID, Long songID) {
        if(!isSongLiked(userID, songID))
            throw new DataAccessException("Song with ID = " + songID +
                    " has not been liked by the user with ID = " + userID + "!");
        dslContext.deleteFrom(Tables.LIKED_SONG)
                .where(Tables.LIKED_SONG.USER_ID.eq(userID).and(Tables.LIKED_SONG.SONG_ID.eq(songID)))
                .execute();
    }

    public void switchLikeSongStatus(Long userID, Long songID) {
        if (isSongLiked(userID, songID)) {
            unlikeSong(userID, songID);
            return;
        }
        likeSong(userID, songID);
    }

    public List<Song> getMusicBandSongsNotLikedByUser(Long userID, Long bandID) {
        return dslContext.select()
                .from(Tables.SONG)
                .join(Tables.AUTHOR_TO_SONG)
                .on(Tables.SONG.ID.eq(Tables.AUTHOR_TO_SONG.SONG_ID))
                .where(Tables.AUTHOR_TO_SONG.MUSIC_BAND_ID.eq(bandID).and(Tables.SONG.ID.notIn(
                                        dslContext.select(Tables.LIKED_SONG.SONG_ID)
                                                .from(Tables.LIKED_SONG)
                                                .where(Tables.LIKED_SONG.USER_ID.eq(userID))
                                )
                        )
                ).fetchInto(Song.class);
    }

    public List<Song> getSingerSongsNotLikedByUser(Long userID, Long singerID) {
        return dslContext.select()
                .from(Tables.SONG)
                .join(Tables.AUTHOR_TO_SONG)
                .on(Tables.SONG.ID.eq(Tables.AUTHOR_TO_SONG.SONG_ID))
                .where(Tables.AUTHOR_TO_SONG.SINGER_ID.eq(singerID)
                        .and(Tables.SONG.ID.notIn(
                                        dslContext.select(Tables.LIKED_SONG.SONG_ID)
                                                .from(Tables.LIKED_SONG)
                                                .where(Tables.LIKED_SONG.USER_ID.eq(userID))
                                )
                        )
                ).fetchInto(Song.class);
    }

    public List<Song> getAlbumSongsNotLikedByUser(Long userID, Long albumID) {
        return dslContext.select()
                .from(Tables.SONG)
                .where(Tables.SONG.ALBUM_ID.eq(albumID).and(
                                Tables.SONG.ID.notIn(
                                        dslContext.select(Tables.LIKED_SONG.SONG_ID)
                                                .from(Tables.LIKED_SONG)
                                                .where(Tables.LIKED_SONG.USER_ID.eq(userID))
                                )
                        )
                ).fetchInto(Song.class);
    }

    public List<Song> getSongsByGenreNotLikedByUser(Long userID, Genre genre) {
        return dslContext.selectFrom(Tables.SONG)
                .where(Tables.SONG.GENRE.eq(genre)
                        .and(Tables.SONG.ID.notIn(
                                dslContext.select(Tables.LIKED_SONG.SONG_ID)
                                        .from(Tables.LIKED_SONG)
                                        .where(Tables.LIKED_SONG.USER_ID.eq(userID))
                        ))
                ).fetchInto(Song.class);
    }

    public List<Song> getSongsNotLikedByUser(Long userID) {
        return dslContext.selectFrom(Tables.SONG)
                .where(Tables.SONG.ID.notIn(
                        dslContext.select(Tables.LIKED_SONG.SONG_ID)
                                .from(Tables.LIKED_SONG)
                                .where(Tables.LIKED_SONG.USER_ID.eq(userID))
                ))
                .fetchInto(Song.class);
    }

    public List<Song> getSongsNotLikedByUser(Long userID, List<Song> without) {
        return dslContext.selectFrom(Tables.SONG)
                .where(Tables.SONG.ID.notIn(
                                dslContext.select(Tables.LIKED_SONG.SONG_ID)
                                        .from(Tables.LIKED_SONG)
                                        .where(Tables.LIKED_SONG.USER_ID.eq(userID))
                        ).and(Tables.SONG.ID.notIn(
                                without.stream().map(Song::getId).toList()
                        ))
                )
                .fetchInto(Song.class);
    }

}
