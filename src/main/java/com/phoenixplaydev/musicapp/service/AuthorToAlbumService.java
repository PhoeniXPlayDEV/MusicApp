package com.phoenixplaydev.musicapp.service;

import com.phoenixplaydev.musicapp.model.tables.pojos.Album;
import com.phoenixplaydev.musicapp.model.tables.pojos.AuthorToAlbum;
import com.phoenixplaydev.musicapp.repository.*;
import org.jooq.exception.DataAccessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuthorToAlbumService {

    @Autowired
    private AuthorToAlbumRepository authorToAlbumRepository;

    @Autowired
    private AlbumRepository albumRepository;

    @Autowired
    private SingerRepository singerRepository;

    @Autowired
    private MusicBandRepository musicBandRepository;

    public List<AuthorToAlbum> getAllAlbumAuthors(Long albumID) {
        if(albumRepository.isAlbumNotExists(albumID))
            throw new DataAccessException("Can't find authors from non-existent album with ID = " + albumID + "!");
        return authorToAlbumRepository.getAllAlbumAuthors(albumID);
    }

    public void addAlbumAuthor(Long albumID, Long singerID, Long bandID) {
        if(albumRepository.isAlbumNotExists(albumID))
            throw new DataAccessException("Can't add author to non-existing album with ID = " + albumID + "!");
        if(singerRepository.isSingerNotExists(singerID))
            throw new DataAccessException("Can't add non-existing singer with ID = " +
                    singerID + "as author to the album with ID = " + albumID + "!");
        if(musicBandRepository.isMusicBandNotExists(bandID))
            throw new DataAccessException("Can't add non-existing music band with ID = " +
                    bandID + "as author to the album with ID = " + albumID + "!");
        authorToAlbumRepository.addAlbumAuthor(albumID, singerID, bandID);
    }

    public void deleteAlbumAuthor(Long albumID, Long singerID, Long bandID) {
        if(albumRepository.isAlbumNotExists(albumID))
            throw new DataAccessException("Can't delete author from non-existing album with ID = " + albumID + "!");
        if(singerRepository.isSingerNotExists(singerID))
            throw new DataAccessException("Can't delete non-existing singer with ID = " +
                    singerID + "as author from the album with ID = " + albumID + "!");
        if(musicBandRepository.isMusicBandNotExists(bandID))
            throw new DataAccessException("Can't delete non-existing music band with ID = " +
                    bandID + "as author from the album with ID = " + albumID + "!");
        authorToAlbumRepository.deleteAlbumAuthor(albumID, singerID, bandID);
    }

    public List<Album> getSingerAlbums(Long singerID) {
        if(singerRepository.isSingerNotExists(singerID))
            throw new DataAccessException("Can't get albums of non-existing singer with ID = " + singerID + "!");
        return authorToAlbumRepository.getSingerAlbums(singerID);
    }

    public List<Album> getMusicBandAlbums(Long bandID) {
        if(musicBandRepository.isMusicBandNotExists(bandID))
            throw new DataAccessException("Can't get albums of non-existing music band with ID = " + bandID + "!");
        return authorToAlbumRepository.getMusicBandAlbums(bandID);
    }

}
