package com.phoenixplaydev.musicapp.repository;

import com.phoenixplaydev.musicapp.model.Tables;
import com.phoenixplaydev.musicapp.model.tables.pojos.Singer;
import org.jooq.DSLContext;
import org.jooq.exception.DataAccessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class SingerRepository {

    @Autowired
    private DSLContext dslContext;

    public boolean isSingerNotExists(Long singerID) {
        return !dslContext.selectFrom(Tables.SINGER)
                .where(Tables.SINGER.ID.eq(singerID))
                .fetch().isNotEmpty();
    }

    public Singer getSingerByID(Long singerID) {
        return dslContext.selectFrom(Tables.SINGER)
                .where(Tables.SINGER.ID.eq(singerID))
                .fetchOneInto(Singer.class);
    }

    public List<Singer> getAllSingers() {
        return dslContext.selectFrom(Tables.SINGER)
                .fetchInto(Singer.class);
    }

    public void addSinger(Singer singer) {
        dslContext.insertInto(
                        Tables.SINGER,
                        Tables.SINGER.NAME,
                        Tables.SINGER.COVER
                ).values(
                        singer.getName(),
                        singer.getCover()
                ).execute();
    }

    public void updateSingerInfo(Singer singer) {
        dslContext.update(Tables.SINGER)
                .set(dslContext.newRecord(Tables.SINGER, singer))
                .where(Tables.SINGER.ID.eq(singer.getId()))
                .returning()
                .fetchOptional()
                .orElseThrow(() ->
                        new DataAccessException(
                                "Can't update singer with ID = " +
                                        singer.getId()))
                .into(Long.class);
    }

    public void deleteSinger(Long singerID) {
        dslContext.deleteFrom(Tables.SINGER)
                .where(Tables.SINGER.ID.eq(singerID)).execute();
    }

}
