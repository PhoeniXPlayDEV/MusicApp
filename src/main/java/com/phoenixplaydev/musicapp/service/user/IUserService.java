package com.phoenixplaydev.musicapp.service.user;

import com.phoenixplaydev.musicapp.model.tables.pojos.*;

import java.util.List;

public interface IUserService {

    boolean isUserExists(Long id);

    boolean isUserExists(String email);

    User getUserByID(Long id);

    User getUserByEmail(String email);

    List<User> getAllUsers();

    void addUser(User user);

    void updateUserInfo(User user);

    List<Song> getLikedSongs(Long id);

    boolean isSongLiked(Long userID, Long songID);

    void likeSong(Long userID, Long songID);

    void unlikeSong(Long userID, Long songID);

    void switchLikeSongStatus(Long userID, Long songID);

    List<Album> getLikedAlbums(Long id);

    boolean isAlbumLiked(Long userID, Long albumID);

    void likeAlbum(Long userID, Long albumID);

    void unlikeAlbum(Long userID, Long albumID);

    void switchLikeAlbumStatus(Long userID, Long albumID);

    List<Singer> getLikedSingers(Long id);

    boolean isSingerLiked(Long userID, Long singerID);

    void likeSinger(Long userID, Long singerID);

    void unlikeSinger(Long userID, Long singerID);

    void switchLikeSingerStatus(Long userID, Long singerID);

    List<MusicBand> getLikedMusicBands(Long id);

    boolean isMusicBandLiked(Long userID, Long musicBandID);

    void likeMusicBand(Long userID, Long musicBandID);

    void unlikeMusicBand(Long userID, Long musicBandID);

    void switchLikeMusicBandStatus(Long userID, Long musicBandID);

    void deleteUser(Long id);

}
