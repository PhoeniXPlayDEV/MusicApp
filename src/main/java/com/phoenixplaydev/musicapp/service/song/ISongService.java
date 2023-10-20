package com.phoenixplaydev.musicapp.service.song;

import com.phoenixplaydev.musicapp.model.tables.pojos.Song;

import java.util.List;

public interface ISongService {

    Song getSongByID(java.lang.Long id);

    List<Song> getAllSongs();

    void addSong(Song song);

    void updateSongInfo(Song song);

    void setAuthor(Long songID, Long singerID, Long bandID);

    void deleteSong(Long songID);

}
