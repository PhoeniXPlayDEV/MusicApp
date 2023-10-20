package com.phoenixplaydev.musicapp.service.Album;

import com.phoenixplaydev.musicapp.model.tables.pojos.Album;

import java.util.List;

public interface IAlbumService {

    Album getAlbumByID(Long id);

    List<Album> getAllAlbums();

    void addAlbum(Album album);

    void updateAlbumInfo(Album album);

    boolean isAlbumExisted(Long id);

    void deleteAlbum(Long id);

}
