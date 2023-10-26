package com.phoenixplaydev.musicapp.repository;

import com.phoenixplaydev.musicapp.model.Tables;
import com.phoenixplaydev.musicapp.model.tables.pojos.MusicBand;
import com.phoenixplaydev.musicapp.model.tables.pojos.Song;
import org.jooq.DSLContext;
import org.jooq.exception.DataAccessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class LikedMusicBandsRepository {

    @Autowired
    private DSLContext dslContext;

    public List<MusicBand> getLikedMusicBands(Long userID) {
        return dslContext.select()
                .from(Tables.LIKED_BAND)
                .join(Tables.MUSIC_BAND)
                .on(Tables.LIKED_BAND.BAND_ID.eq(Tables.MUSIC_BAND.ID))
                .where(Tables.LIKED_BAND.USER_ID.eq(userID))
                .fetchInto(MusicBand.class);
    }

    public boolean isMusicBandLiked(Long userID, Long musicBandID) {
        return dslContext.selectFrom(Tables.LIKED_BAND)
                .where(Tables.LIKED_BAND.USER_ID.eq(userID).and(Tables.LIKED_BAND.BAND_ID.eq(musicBandID)))
                .fetch().isNotEmpty();
    }

    public void likeMusicBand(Long userID, Long musicBandID) {
        if(isMusicBandLiked(userID, musicBandID))
            throw new DataAccessException("Music band with ID = " + musicBandID +
                    " already liked by the user with ID = " + userID + "!");
        dslContext.insertInto(
                Tables.LIKED_BAND,
                Tables.LIKED_BAND.USER_ID,
                Tables.LIKED_BAND.BAND_ID
        ).values(
                userID,
                musicBandID
        ).execute();
    }

    public void unlikeMusicBand(Long userID, Long musicBandID) {
        if(!isMusicBandLiked(userID, musicBandID))
            throw new DataAccessException("Music band with ID = " + musicBandID +
                    " has not been liked by the user with ID = " + userID + "!");
        dslContext.deleteFrom(Tables.LIKED_BAND)
                .where(Tables.LIKED_BAND.USER_ID.eq(userID).and(Tables.LIKED_BAND.BAND_ID.eq(musicBandID)))
                .execute();
    }

    public void switchLikeMusicBandStatus(Long userID, Long musicBandID) {
        if(isMusicBandLiked(userID, musicBandID)) {
            unlikeMusicBand(userID, musicBandID);
            return;
        }
        likeMusicBand(userID, musicBandID);
    }

}
