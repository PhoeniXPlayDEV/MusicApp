package com.phoenixplaydev.musicapp.repository;

import com.phoenixplaydev.musicapp.model.Tables;
import com.phoenixplaydev.musicapp.model.tables.pojos.Album;
import com.phoenixplaydev.musicapp.model.tables.pojos.Song;
import org.jooq.DSLContext;
import org.jooq.exception.DataAccessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class LikedAlbumsRepository {

    @Autowired
    private DSLContext dslContext;

    public List<Album> getLikedAlbums(Long userID) {
        return dslContext.select()
                .from(Tables.LIKED_ALBUM)
                .join(Tables.ALBUM)
                .on(Tables.LIKED_ALBUM.ALBUM_ID.eq(Tables.ALBUM.ID))
                .where(Tables.LIKED_ALBUM.USER_ID.eq(userID))
                .fetchInto(Album.class);
    }

    public boolean isAlbumLiked(Long userID, Long albumID) {
        return dslContext.selectFrom(Tables.LIKED_ALBUM)
                .where(Tables.LIKED_ALBUM.USER_ID.eq(userID).and(Tables.LIKED_ALBUM.ALBUM_ID.eq(albumID)))
                .fetch().isNotEmpty();
    }

    public void likeAlbum(Long userID, Long albumID) {
        if(isAlbumLiked(userID, albumID))
            throw new DataAccessException("Album with ID = " + albumID +
                    " already liked by the user with ID = " + userID + "!");
        dslContext.insertInto(
                Tables.LIKED_ALBUM,
                Tables.LIKED_ALBUM.USER_ID,
                Tables.LIKED_ALBUM.ALBUM_ID
        ).values(
                userID,
                albumID
        ).execute();
    }

    public void unlikeAlbum(Long userID, Long albumID) {
        if(!isAlbumLiked(userID, albumID))
            throw new DataAccessException("Album with ID = " + albumID +
                    " has not been liked by the user with ID = " + userID + "!");
        dslContext.deleteFrom(Tables.LIKED_ALBUM)
                .where(Tables.LIKED_ALBUM.USER_ID.eq(userID).and(Tables.LIKED_ALBUM.ALBUM_ID.eq(albumID)))
                .execute();
    }

    public void switchLikeAlbumStatus(Long userID, Long albumID) {
        if(isAlbumLiked(userID, albumID)) {
            unlikeAlbum(userID, albumID);
            return;
        }
        likeAlbum(userID, albumID);
    }

}
