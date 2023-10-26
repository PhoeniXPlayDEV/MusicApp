package com.phoenixplaydev.musicapp.service;

import com.phoenixplaydev.musicapp.model.tables.pojos.Song;
import com.phoenixplaydev.musicapp.repository.LikedSongsRepository;
import com.phoenixplaydev.musicapp.repository.SongRepository;
import com.phoenixplaydev.musicapp.repository.UserRepository;
import org.jooq.exception.DataAccessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LikedSongsService {

    @Autowired
    private LikedSongsRepository likedSongsRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SongRepository songRepository;

    public List<Song> getLikedSongs(Long userID) {
        if(userRepository.isUserNotExists(userID))
            throw new DataAccessException("Can't get the liked songs of a non-existent user with an ID = " +
                    userID + "!");
        return likedSongsRepository.getLikedSongs(userID);
    }

    public void switchLikeSongStatus(Long userID, Long songID) {
        if(userRepository.isUserNotExists(userID))
            throw new DataAccessException("Can't switch the song status for a non-existent user with an ID = " +
                    userID + "!");
        if(songRepository.isSongNotExists(songID))
            throw new DataAccessException("Can't switch status of non existing song with ID = " +
                    songID + " for a user with ID = " + userID + "!");
        likedSongsRepository.switchLikeSongStatus(userID, songID);
    }

    public int getAmountOfLikedSongs(Long userID) {
        return getLikedSongs(userID).size();
    }

}
