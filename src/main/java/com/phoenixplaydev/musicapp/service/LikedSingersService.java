package com.phoenixplaydev.musicapp.service;

import com.phoenixplaydev.musicapp.model.tables.pojos.Singer;
import com.phoenixplaydev.musicapp.repository.LikedSingersRepository;
import com.phoenixplaydev.musicapp.repository.SingerRepository;
import com.phoenixplaydev.musicapp.repository.UserRepository;
import org.jooq.exception.DataAccessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LikedSingersService {

    @Autowired
    private LikedSingersRepository likedSingersRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SingerRepository singerRepository;

    public List<Singer> getLikedSingers(Long userID) {
        if(userRepository.isUserNotExists(userID))
            throw new DataAccessException("Can't get the liked singer of a non-existent user with an ID = " +
                    userID + "!");
        return likedSingersRepository.getLikedSingers(userID);
    }

    public void switchLikeSingerStatus(Long userID, Long singerID) {
        if(userRepository.isUserNotExists(userID))
            throw new DataAccessException("Can't switch the singer status for a non-existent user with an ID = " +
                    userID + "!");
        if(singerRepository.isSingerNotExists(singerID))
            throw new DataAccessException("Can't switch status of non existing singer with ID = " +
                    singerID + " for a user with ID = " + userID + "!");
        likedSingersRepository.switchLikeSingerStatus(userID, singerID);
    }

}
