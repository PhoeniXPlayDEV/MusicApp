package com.phoenixplaydev.musicapp.service;

import com.phoenixplaydev.musicapp.model.tables.pojos.Album;
import com.phoenixplaydev.musicapp.repository.AlbumRepository;
import org.jooq.exception.DataAccessException;
import org.jooq.exception.NoDataFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AlbumService {

    @Autowired
    private AlbumRepository albumRepository;

    public Album getAlbumByID(Long albumID) {
        if(albumRepository.isAlbumNotExists(albumID))
            throw new NoDataFoundException("Album with ID = " + albumID + " not found!");
        return albumRepository.getAlbumByID(albumID);
    }

    public List<Album> getAllAlbums() {
        return albumRepository.getAllAlbums();
    }

    public void addAlbum(String name, byte[] cover) {
        Album album = new Album();
        if(name == null || name.isBlank())
            throw new IllegalArgumentException("Album name can't be null or empty!");
        album.setName(name);
        album.setCover(cover);
        albumRepository.addAlbum(album);
    }

    public void updateAlbumInfo(Long albumID, String name, byte[] cover) {
        if(albumRepository.isAlbumNotExists(albumID))
            throw new DataAccessException("Can't update non-existing album with ID = " + albumID + "!");
        Album album = albumRepository.getAlbumByID(albumID);

        if(name != null) {
            if (name.isBlank())
                throw new IllegalArgumentException("Album name can't be empty!");
            album.setName(name);
        }
        if(cover != null)
            album.setCover(cover);

        albumRepository.updateAlbumInfo(album);
    }

    public void deleteAlbum(Long albumID) {
        if(albumRepository.isAlbumNotExists(albumID))
            throw new DataAccessException("Can't delete non-existing album with ID = " + albumID + "!" );
        albumRepository.deleteAlbum(albumID);
    }

}
