package com.phoenixplaydev.musicapp.service.Album;

import com.phoenixplaydev.musicapp.model.Tables;
import com.phoenixplaydev.musicapp.model.tables.pojos.Album;
import org.jooq.DSLContext;
import org.jooq.exception.DataAccessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class AlbumService implements IAlbumService {

    @Autowired
    private DSLContext dslContext;

    @Override
    public Album getAlbumByID(Long id) {
        return dslContext.selectFrom(Tables.ALBUM)
                .where(Tables.ALBUM.ID.eq(id))
                .fetchOptional()
                .orElseThrow(() ->
                        new DataAccessException(
                                "Can't find album with ID = " + id))
                .into(Album.class);
    }

    @Override
    public List<Album> getAllAlbums() {
        return dslContext.selectFrom(Tables.ALBUM)
                .fetchInto(Album.class);
    }

    @Override
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

    @Override
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

    @Override
    public boolean isAlbumExisted(Long id) {
        return dslContext.selectFrom(Tables.ALBUM)
                .where(Tables.ALBUM.ID.eq(id))
                .fetch().isNotEmpty();
    }

    @Override
    public void deleteAlbum(Long id) {
        if(!isAlbumExisted(id))
            throw new DataAccessException("The album delete with non-existing ID = " + id + "!" );
        dslContext.deleteFrom(Tables.ALBUM)
                .where(Tables.ALBUM.ID.eq(id))
                .execute();
    }

}
