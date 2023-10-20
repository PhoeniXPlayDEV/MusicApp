package com.phoenixplaydev.musicapp.service.singer;

import com.phoenixplaydev.musicapp.model.Tables;
import com.phoenixplaydev.musicapp.model.tables.pojos.Singer;
import org.jooq.DSLContext;
import org.jooq.exception.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SingerService implements ISingerService {

    private DSLContext dslContext;

    @Override
    public Long getSingerByID(java.lang.Long id) {
        return dslContext.selectFrom(Tables.SINGER)
                .where(Tables.SINGER.ID.eq(id))
                .fetchOptional()
                .orElseThrow(() ->
                        new DataAccessException(
                                "Can't find singer with ID = " + id))
                .into(Long.class);
    }

    @Override
    public List<Long> getAllSingers() {
        return dslContext.selectFrom(Tables.SINGER)
                .fetchInto(Long.class);
    }

    @Override
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

    @Override
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

}
