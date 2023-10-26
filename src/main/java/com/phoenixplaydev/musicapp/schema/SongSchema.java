package com.phoenixplaydev.musicapp.schema;

import com.phoenixplaydev.musicapp.configuration.security.RolesChecker;
import com.phoenixplaydev.musicapp.model.tables.pojos.Song;
import com.phoenixplaydev.musicapp.service.SongService;
import io.leangen.graphql.annotations.GraphQLArgument;
import io.leangen.graphql.annotations.GraphQLMutation;
import io.leangen.graphql.annotations.GraphQLQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SongSchema {

    @Autowired
    private SongService songService;

    @Autowired
    private RolesChecker rolesChecker;

    @GraphQLQuery(name = "getSongByID")
    public Song getSongByID(@GraphQLArgument(name = "song_id") Long songID) {
        return songService.getSongByID(songID);
    }

    @GraphQLQuery(name = "getAllSongs")
    public List<Song> getAllSongs() {
        return songService.getAllSongs();
    }

    @GraphQLMutation(name = "updateSongInfo")
    public void updateSongInfo(@GraphQLArgument(name = "song_id") Long songID,
                               @GraphQLArgument(name = "name") String name,
                               @GraphQLArgument(name = "cover") byte[] cover,
                               @GraphQLArgument(name = "length") double length,
                               @GraphQLArgument(name = "genre") String genre,
                               @GraphQLArgument(name = "file") byte[] file,
                               @GraphQLArgument(name = "album_id") Long album_id) {
        rolesChecker.userHasAdminRole();
        songService.updateSongInfo(songID, name, cover, length, name, file, album_id);
    }

    @GraphQLMutation(name = "addSong")
    public void addSong(@GraphQLArgument(name = "name") String name,
                        @GraphQLArgument(name = "cover") byte[] cover,
                        @GraphQLArgument(name = "length") double length,
                        @GraphQLArgument(name = "genre") String genre,
                        @GraphQLArgument(name = "file") byte[] file,
                        @GraphQLArgument(name = "album_id") Long album_id) {
        rolesChecker.userHasAdminRole();
        songService.addSong(name, cover, length, genre, file, album_id);
    }

    @GraphQLMutation(name = "deleteSong")
    public void deleteSong(@GraphQLArgument(name = "deleteSong") Long songID) {
        songService.deleteSong(songID);
    }

    @GraphQLQuery(name = "getSongsByGenre")
    public List<Song> getSongsByGenre(@GraphQLArgument(name = "genre") String genre) {
        return songService.getSongsByGenre(genre);
    }

}
