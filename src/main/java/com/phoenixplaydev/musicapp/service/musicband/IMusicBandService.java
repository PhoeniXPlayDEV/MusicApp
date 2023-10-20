package com.phoenixplaydev.musicapp.service.musicband;

import com.phoenixplaydev.musicapp.model.tables.pojos.MusicBand;

import java.util.List;

public interface IMusicBandService {

    MusicBand getMusicBandByID(Long id);

    List<MusicBand> getAllMusicBands();

    void addMusicBand(MusicBand musicBand);

    void updateMusicBandInfo(MusicBand musicBand);

    void deleteMusicBand(Long id);

    void addSingerToMusicBand(Long id);

    void deleteSingerFromMusicBand(Long id);

}
