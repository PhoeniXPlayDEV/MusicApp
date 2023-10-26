package com.phoenixplaydev.musicapp.service;

import com.phoenixplaydev.musicapp.model.tables.pojos.AuthorToSong;
import com.phoenixplaydev.musicapp.model.tables.pojos.Song;
import com.phoenixplaydev.musicapp.repository.AuthorToSongRepository;
import com.phoenixplaydev.musicapp.repository.MusicBandRepository;
import com.phoenixplaydev.musicapp.repository.SingerRepository;
import com.phoenixplaydev.musicapp.repository.SongRepository;
import org.jooq.exception.DataAccessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuthorToSongService {

    @Autowired
    private AuthorToSongRepository authorToSongRepository;

    @Autowired
    private SongRepository songRepository;

    @Autowired
    private SingerRepository singerRepository;

    @Autowired
    private MusicBandRepository musicBandRepository;

    public List<AuthorToSong> getAllSongAuthors(Long songID) {
        if(songRepository.isSongNotExists(songID))
            throw new DataAccessException("Can't find authors from non-existent song with ID = " + songID + "!");
        return authorToSongRepository.getAllSongAuthors(songID);
    }

    public void addSongAuthor(Long songID, Long singerID, Long bandID) {
        if(songRepository.isSongNotExists(songID))
            throw new DataAccessException("Can't add author to non-existing song with ID = " + songID + "!");
        if(singerRepository.isSingerNotExists(singerID))
            throw new DataAccessException("Can't add non-existing singer with ID = " +
                    singerID + "as author to the song with ID = " + songID + "!");
        if(musicBandRepository.isMusicBandNotExists(bandID))
            throw new DataAccessException("Can't add non-existing music band with ID = " +
                    bandID + "as author to the song with ID = " + songID + "!");
        authorToSongRepository.addSongAuthor(songID, singerID, bandID);
    }

    public void deleteSongAuthor(Long songID, Long singerID, Long bandID) {
        if(songRepository.isSongNotExists(songID))
            throw new DataAccessException("Can't delete author from non-existing song with ID = " + songID + "!");
        if(singerRepository.isSingerNotExists(singerID))
            throw new DataAccessException("Can't delete non-existing singer with ID = " +
                    singerID + "as author from the song with ID = " + songID + "!");
        if(musicBandRepository.isMusicBandNotExists(bandID))
            throw new DataAccessException("Can't delete non-existing music band with ID = " +
                    bandID + "as author from the song with ID = " + songID + "!");
        authorToSongRepository.deleteSongAuthor(songID, singerID, bandID);
    }

    public List<Song> getSingerSongs(Long singerID) {
        if(singerRepository.isSingerNotExists(singerID))
            throw new DataAccessException("Can't get songs of non-existing singer with ID = " + singerID + "!");
        return authorToSongRepository.getSingerSongs(singerID);
    }

    public List<Song> getMusicBandSongs(Long bandID) {
        if(musicBandRepository.isMusicBandNotExists(bandID))
            throw new DataAccessException("Can't get songs of non-existing music band with ID = " + bandID + "!");
        return authorToSongRepository.getMusicBandSongs(bandID);
    }

}
