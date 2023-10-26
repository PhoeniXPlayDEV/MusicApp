package com.phoenixplaydev.musicapp.service;

import com.phoenixplaydev.musicapp.model.enums.Genre;
import com.phoenixplaydev.musicapp.model.tables.pojos.Song;
import com.phoenixplaydev.musicapp.repository.AlbumRepository;
import com.phoenixplaydev.musicapp.repository.SongRepository;
import org.jooq.exception.DataAccessException;
import org.jooq.exception.NoDataFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class SongService {

    @Autowired
    private SongRepository songRepository;

    @Autowired
    private AlbumRepository albumRepository;

    public Song getSongByID(Long songID) {
        if(songRepository.isSongNotExists(songID))
            throw new NoDataFoundException("Song with ID = " + songID + " not found!");
        return songRepository.getSongByID(songID);
    }

    public List<Song> getAllSongs() {
        return songRepository.getAllSongs();
    }

    public void addSong(String name, byte[] cover, double length,
                        String genre, byte[] file, Long album_id) {

        Song song = new Song();
        if(name == null || name.isBlank())
            throw new IllegalArgumentException("Song name can't be null or empty!");
        song.setName(name);
        song.setCover(cover);
        if(length <= 0)
            throw new IllegalArgumentException("Song duration can't be less or equals zero!");
        song.setLength(length);
        song.setGenre(getGenreByName(genre));
        if(file == null || file.length == 0)
            throw new IllegalArgumentException("File can't null or empty!");
        song.setFile(file);

        if(albumRepository.isAlbumNotExists(album_id))
            throw new DataAccessException("Can't add song to not existing album with ID = " + album_id + "!");
        song.setAlbumId(album_id);

        songRepository.addSong(song);
    }

    public void updateSongInfo(Long songID, String name, byte[] cover, double length,
                               String genre, byte[] file, Long album_id) {
        if(songRepository.isSongNotExists(songID))
            throw new DataAccessException("Can't update non-existing song with ID = " + songID + "!");
        Song song = songRepository.getSongByID(songID);
        if(name != null) {
            if(name.isBlank())
                throw new IllegalArgumentException("Song name can't be empty!");
            song.setName(name);
        }
        if(cover != null)
            song.setCover(cover);
        if(length <= 0)
            throw new IllegalArgumentException("Song duration can't be less or equals zero!");
        song.setLength(length);
        if(genre != null) {
            song.setGenre(getGenreByName(genre));
        }
        if(cover != null){
            if(cover.length == 0)
                throw new IllegalArgumentException("File can't null or empty!");
            song.setFile(file);
        }
        if(album_id != 0) {
            if(albumRepository.isAlbumNotExists(album_id))
                throw new DataAccessException("Can't add song to not existing album with ID = " + album_id + "!");
            song.setAlbumId(album_id);
        }
    }

    public void deleteSong(Long songID) {
        if(songRepository.isSongNotExists(songID))
            throw new DataAccessException("Can't delete non-existing song with ID = " + songID + "!");
        songRepository.deleteSong(songID);
    }

    public List<Song> getSongsByGenre(String genre) {
        return songRepository.getSongsByGenre(getGenreByName(genre));
    }

    public Genre getGenreByName(String genre) {
        if(genre == null || !Arrays.stream(Genre.values()).map(Genre::toString).toList().contains(genre))
            throw new IllegalArgumentException("Can't find genre = '" + genre + "'!");
        return Genre.valueOf(genre);
    }

}
