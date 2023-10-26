package com.phoenixplaydev.musicapp.repository;

import com.phoenixplaydev.musicapp.model.Tables;
import com.phoenixplaydev.musicapp.model.tables.pojos.MusicBand;
import org.jooq.DSLContext;
import org.jooq.exception.DataAccessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class MusicBandRepository {

    @Autowired
    private DSLContext dslContext;

    public boolean isMusicBandNotExists(Long bandID) {
        return !dslContext.selectFrom(Tables.MUSIC_BAND)
                .where(Tables.MUSIC_BAND.ID.eq(bandID))
                .fetch().isNotEmpty();
    }

    public MusicBand getMusicBandByID(Long bandID) {
        return dslContext.selectFrom(Tables.MUSIC_BAND)
                .where(Tables.MUSIC_BAND.ID.eq(bandID))
                .fetchOneInto(MusicBand.class);
    }

    public List<MusicBand> getAllMusicBands() {
        return dslContext.selectFrom(Tables.MUSIC_BAND)
                .fetchInto(MusicBand.class);
    }

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

    public void deleteMusicBand(Long bandID) {
        dslContext.deleteFrom(Tables.MUSIC_BAND)
                .where(Tables.MUSIC_BAND.ID.eq(bandID)).execute();
    }

    public void addSingerToMusicBand(Long bandID) {
        //Where the table for that?
    }

    public void deleteSingerFromMusicBand(Long bandID) {
        //Where the table for that?
    }

}
