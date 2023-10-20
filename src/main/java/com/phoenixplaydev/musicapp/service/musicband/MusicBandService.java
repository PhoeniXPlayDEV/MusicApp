package com.phoenixplaydev.musicapp.service.musicband;

import com.phoenixplaydev.musicapp.model.Tables;
import com.phoenixplaydev.musicapp.model.tables.pojos.MusicBand;
import org.jooq.DSLContext;
import org.jooq.exception.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MusicBandService implements IMusicBandService {

    private DSLContext dslContext;

    @Override
    public MusicBand getMusicBandByID(Long id) {
        return dslContext.selectFrom(Tables.MUSIC_BAND)
                .where(Tables.MUSIC_BAND.ID.eq(id))
                .fetchOptional()
                .orElseThrow(() ->
                        new DataAccessException(
                                "Can't find music band with ID = " + id))
                .into(MusicBand.class);
    }

    @Override
    public List<MusicBand> getAllMusicBands() {
        return dslContext.selectFrom(Tables.MUSIC_BAND)
                .fetchInto(MusicBand.class);
    }

    @Override
    public void addMusicBand(MusicBand musicBand) {
        dslContext.insertInto(
                        Tables.MUSIC_BAND,
                        Tables.MUSIC_BAND.NAME,
                        Tables.MUSIC_BAND.COVER
                ).values(
                        musicBand.getName(),
                        musicBand.getCover()
                ).execute();
    }

    @Override
    public void updateMusicBandInfo(MusicBand musicBand) {
        dslContext.update(Tables.MUSIC_BAND)
                .set(dslContext.newRecord(Tables.MUSIC_BAND, musicBand))
                .where(Tables.MUSIC_BAND.ID.eq(musicBand.getId()))
                .returning()
                .fetchOptional()
                .orElseThrow(() ->
                        new DataAccessException(
                                "Can't update music band with ID = " +
                                        musicBand.getId()))
                .into(MusicBand.class);
    }

    @Override
    public void deleteMusicBand(Long id) {
        dslContext.deleteFrom(Tables.MUSIC_BAND)
                .where(Tables.MUSIC_BAND.ID.eq(id)).execute();
    }

    @Override
    public void addSingerToMusicBand(Long id) {
        //Where the table for that?
    }

    @Override
    public void deleteSingerFromMusicBand(Long id) {
        //Where the table for that?
    }

}
