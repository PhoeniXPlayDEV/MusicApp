package com.phoenixplaydev.musicapp.repository;

import com.phoenixplaydev.musicapp.model.Tables;
import com.phoenixplaydev.musicapp.model.enums.Genre;
import com.phoenixplaydev.musicapp.model.tables.pojos.Singer;
import com.phoenixplaydev.musicapp.model.tables.pojos.Song;
import org.jooq.DSLContext;
import org.jooq.exception.DataAccessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class LikedSingersRepository {

    @Autowired
    private DSLContext dslContext;

    public List<Singer> getLikedSingers(Long userID) {
        return dslContext.select()
                .from(Tables.LIKED_SINGER)
                .join(Tables.SINGER)
                .on(Tables.LIKED_SINGER.SINGER_ID.eq(Tables.SINGER.ID))
                .where(Tables.LIKED_SINGER.USER_ID.eq(userID))
                .fetchInto(Singer.class);
    }

    public boolean isSingerLiked(Long userID, Long singerID) {
        return dslContext.selectFrom(Tables.LIKED_SINGER)
                .where(Tables.LIKED_SINGER.USER_ID.eq(userID).and(Tables.LIKED_SINGER.SINGER_ID.eq(singerID)))
                .fetch().isNotEmpty();
    }

    public void likeSinger(Long userID, Long singerID) {
        if(isSingerLiked(userID, singerID))
            throw new DataAccessException("Singer with ID = " + singerID +
                    " already liked by the user with ID = " + userID + "!");
        dslContext.insertInto(
                Tables.LIKED_SINGER,
                Tables.LIKED_SINGER.USER_ID,
                Tables.LIKED_SINGER.SINGER_ID
        ).values(
                userID,
                singerID
        ).execute();
    }

    public void unlikeSinger(Long userID, Long singerID) {
        if(!isSingerLiked(userID, singerID))
            throw new DataAccessException("Singer with ID = " + singerID +
                    " has not been liked by the user with ID = " + userID + "!");
        dslContext.deleteFrom(Tables.LIKED_SINGER)
                .where(Tables.LIKED_SINGER.USER_ID.eq(userID).and(Tables.LIKED_SINGER.SINGER_ID.eq(singerID)))
                .execute();
    }

    public void switchLikeSingerStatus(Long userID, Long singerID) {
        if(isSingerLiked(userID, singerID)) {
            unlikeSinger(userID, singerID);
            return;
        }
        likeSinger(userID, singerID);
    }

}
