package com.phoenixplaydev.musicapp.service;

import com.phoenixplaydev.musicapp.model.tables.pojos.MusicBand;
import com.phoenixplaydev.musicapp.repository.MusicBandRepository;
import org.jooq.exception.DataAccessException;
import org.jooq.exception.NoDataFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MusicBandService {

    @Autowired
    private MusicBandRepository musicBandRepository;

    public MusicBand getMusicBandByID(Long bandID) {
        if(musicBandRepository.isMusicBandNotExists(bandID))
            throw new NoDataFoundException("Music band with ID = " + bandID + " not found!");
        return musicBandRepository.getMusicBandByID(bandID);
    }

    public List<MusicBand> getAllMusicBands() {
        return musicBandRepository.getAllMusicBands();
    }

    public void addMusicBand(String name, byte[] cover) {
        if(name == null || name.isBlank())
            throw new IllegalArgumentException("Music band name can't be null or empty!");
        MusicBand musicBand = new MusicBand();
        musicBand.setName(name);
        musicBand.setCover(cover);
        musicBandRepository.addMusicBand(musicBand);
    }

    public void updateMusicBandInfo(Long bandID, String name, byte[] cover) {
        if(musicBandRepository.isMusicBandNotExists(bandID))
            throw new DataAccessException("Can't update non-existing music band with ID = " + bandID + "!");

        MusicBand musicBand = musicBandRepository.getMusicBandByID(bandID);
        if(name != null) {
            if(name.isBlank())
                throw new IllegalArgumentException("Music band name can't be empty!");
            musicBand.setName(name);
        }
        if(cover != null)
            musicBand.setCover(cover);

        musicBandRepository.updateMusicBandInfo(musicBand);
    }

    public void deleteMusicBand(Long bandID) {
        if(musicBandRepository.isMusicBandNotExists(bandID))
            throw new DataAccessException("Can't delete non-existing music band with ID = " + bandID + "!");
        musicBandRepository.deleteMusicBand(bandID);
    }

}
