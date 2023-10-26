package com.phoenixplaydev.musicapp.repository;

import com.phoenixplaydev.musicapp.model.Tables;
import com.phoenixplaydev.musicapp.model.tables.pojos.Album;
import org.jooq.DSLContext;
import org.jooq.exception.DataAccessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class AlbumRepository {

    @Autowired
    private DSLContext dslContext;

    public boolean isAlbumNotExists(Long albumID) {
        return !dslContext.selectFrom(Tables.ALBUM)
                .where(Tables.ALBUM.ID.eq(albumID))
                .fetch().isNotEmpty();
    }

    public Album getAlbumByID(Long albumID) {
        return dslContext.selectFrom(Tables.ALBUM)
                .where(Tables.ALBUM.ID.eq(albumID))
                .fetchOneInto(Album.class);
    }

    public List<Album> getAllAlbums() {
        return dslContext.selectFrom(Tables.ALBUM)
                .fetchInto(Album.class);
    }

    public void addAlbum(Album album) {
        dslContext.insertInto(
                        Tables.ALBUM,
                        Tables.ALBUM.NAME,
                        Tables.ALBUM.COVER
                ).values(
                        album.getName(),
                        album.getCover()
                ).execute();
    }

    public void updateAlbumInfo(Album album) {
        dslContext.update(Tables.ALBUM)
                .set(dslContext.newRecord(Tables.ALBUM, album))
                .where(Tables.ALBUM.ID.eq(album.getId()))
                .returning()
                .fetchOptional()
                .orElseThrow(() ->
                        new DataAccessException(
                                "Can't update album with ID = " +
                                        album.getId()))
                .into(Album.class);
    }

    public void deleteAlbum(Long albumID) {
        dslContext.deleteFrom(Tables.ALBUM)
                .where(Tables.ALBUM.ID.eq(albumID))
                .execute();
    }

}
