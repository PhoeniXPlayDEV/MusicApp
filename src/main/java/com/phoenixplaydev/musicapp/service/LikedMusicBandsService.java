package com.phoenixplaydev.musicapp.service;

import com.phoenixplaydev.musicapp.model.tables.pojos.MusicBand;
import com.phoenixplaydev.musicapp.repository.LikedMusicBandsRepository;
import com.phoenixplaydev.musicapp.repository.MusicBandRepository;
import com.phoenixplaydev.musicapp.repository.UserRepository;
import org.jooq.exception.DataAccessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LikedMusicBandsService {

    @Autowired
    private LikedMusicBandsRepository likedMusicBandsRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MusicBandRepository musicBandRepository;

    public List<MusicBand> getLikedMusicBands(Long userID) {
        if(userRepository.isUserNotExists(userID))
            throw new DataAccessException("Can't get the liked music band of a non-existent user with an ID = " +
                    userID + "!");
        return likedMusicBandsRepository.getLikedMusicBands(userID);
    }

    public void switchLikeMusicBandStatus(Long userID, Long musicBandID) {
        if(userRepository.isUserNotExists(userID))
            throw new DataAccessException("Can't switch the music band status for a non-existent user with an ID = " +
                    userID + "!");
        if(musicBandRepository.isMusicBandNotExists(musicBandID))
            throw new DataAccessException("Can't switch status of non existing music band with ID = " +
                    musicBandID + " for a user with ID = " + userID + "!");
        likedMusicBandsRepository.switchLikeMusicBandStatus(userID, musicBandID);
    }

}
