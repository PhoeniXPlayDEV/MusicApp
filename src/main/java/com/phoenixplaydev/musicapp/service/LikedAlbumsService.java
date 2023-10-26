package com.phoenixplaydev.musicapp.service;

import com.phoenixplaydev.musicapp.model.tables.pojos.Album;
import com.phoenixplaydev.musicapp.repository.AlbumRepository;
import com.phoenixplaydev.musicapp.repository.LikedAlbumsRepository;
import com.phoenixplaydev.musicapp.repository.UserRepository;
import org.jooq.exception.DataAccessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LikedAlbumsService {

    @Autowired
    private LikedAlbumsRepository likedAlbumsRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AlbumRepository albumRepository;

    public List<Album> getLikedAlbums(Long userID) {
        if(userRepository.isUserNotExists(userID))
            throw new DataAccessException("Can't get the liked songs of a non-existent user with an ID = " +
                    userID + "!");
        return likedAlbumsRepository.getLikedAlbums(userID);
    }

    public void switchLikeAlbumStatus(Long userID, Long albumID) {
        if(userRepository.isUserNotExists(userID))
            throw new DataAccessException("Can't switch the album status for a non-existent user with an ID = " +
                    userID + "!");
        if(albumRepository.isAlbumNotExists(albumID))
            throw new DataAccessException("Can't switch status of non existing album with ID = " +
                    albumID + " for a user with ID = " + userID + "!");
        likedAlbumsRepository.switchLikeAlbumStatus(userID, albumID);
    }

}
