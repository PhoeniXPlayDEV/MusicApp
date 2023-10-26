package com.phoenixplaydev.musicapp.repository;

import com.phoenixplaydev.musicapp.model.Tables;
import com.phoenixplaydev.musicapp.model.tables.pojos.Album;
import com.phoenixplaydev.musicapp.model.tables.pojos.AuthorToAlbum;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class AuthorToAlbumRepository {

    @Autowired
    private DSLContext dslContext;

    public List<AuthorToAlbum> getAllAlbumAuthors(Long albumID) {
        return dslContext.selectFrom(Tables.AUTHOR_TO_ALBUM)
                .where(Tables.AUTHOR_TO_ALBUM.ALBUM_ID.eq(albumID))
                .fetchInto(AuthorToAlbum.class);
    }

    public void addAlbumAuthor(Long albumID, Long singerID, Long bandID) {
        dslContext.insertInto(
                Tables.AUTHOR_TO_ALBUM,
                Tables.AUTHOR_TO_ALBUM.ALBUM_ID,
                Tables.AUTHOR_TO_ALBUM.SINGER_ID,
                Tables.AUTHOR_TO_ALBUM.MUSIC_BAND_ID
        ).values(
                albumID,
                singerID,
                bandID
        ).execute();
    }

    public void deleteAlbumAuthor(Long albumID, Long singerID, Long bandID) {
        dslContext.deleteFrom(Tables.AUTHOR_TO_ALBUM)
                .where(Tables.AUTHOR_TO_ALBUM.ALBUM_ID.eq(albumID)
                        .and(Tables.AUTHOR_TO_ALBUM.SINGER_ID.eq(singerID))
                        .and(Tables.AUTHOR_TO_ALBUM.MUSIC_BAND_ID.eq(bandID))
                ).execute();
    }

    public List<Album> getSingerAlbums(Long singerID) {
        return dslContext.selectFrom(Tables.AUTHOR_TO_ALBUM)
                .where(Tables.AUTHOR_TO_ALBUM.SINGER_ID.eq(singerID))
                .fetchInto(Album.class);
    }

    public List<Album> getMusicBandAlbums(Long bandID) {
        return dslContext.selectFrom(Tables.AUTHOR_TO_ALBUM)
                .where(Tables.AUTHOR_TO_ALBUM.MUSIC_BAND_ID.eq(bandID))
                .fetchInto(Album.class);
    }

}
