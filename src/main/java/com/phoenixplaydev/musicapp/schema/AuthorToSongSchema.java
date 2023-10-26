package com.phoenixplaydev.musicapp.schema;

import com.phoenixplaydev.musicapp.configuration.security.RolesChecker;
import com.phoenixplaydev.musicapp.model.tables.pojos.AuthorToSong;
import com.phoenixplaydev.musicapp.model.tables.pojos.Song;
import com.phoenixplaydev.musicapp.service.AuthorToSongService;
import io.leangen.graphql.annotations.GraphQLArgument;
import io.leangen.graphql.annotations.GraphQLMutation;
import io.leangen.graphql.annotations.GraphQLQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuthorToSongSchema {

    @Autowired
    private AuthorToSongService authorToSongService;

    @Autowired
    private RolesChecker rolesChecker;

    @GraphQLQuery(name = "getAllSongAuthors")
    public List<AuthorToSong> getAllSongAuthors(@GraphQLArgument(name = "song_id") Long songID) {
        return authorToSongService.getAllSongAuthors(songID);
    }

    @GraphQLMutation(name = "addSongAuthor")
    public void addSongAuthor(@GraphQLArgument(name = "song_id") Long songID,
                               @GraphQLArgument(name = "singer_id") Long singerID,
                               @GraphQLArgument(name = "band_id") Long bandID) {
        rolesChecker.userHasAdminRole();
        authorToSongService.addSongAuthor(songID, singerID, bandID);
    }

    @GraphQLMutation(name = "deleteSongAuthor")
    public void deleteSongAuthor(@GraphQLArgument(name = "song_id") Long songID,
                                  @GraphQLArgument(name = "singer_id") Long singerID,
                                  @GraphQLArgument(name = "band_id") Long bandID) {
        rolesChecker.userHasAdminRole();
        authorToSongService.deleteSongAuthor(songID, singerID, bandID);
    }

    @GraphQLQuery(name = "getSingerSongs")
    public List<Song> getSingerSongs(@GraphQLArgument(name = "singer_id") Long singerID) {
        return authorToSongService.getSingerSongs(singerID);
    }

    @GraphQLQuery(name = "getMusicBandSongs")
    public List<Song> getMusicBandSongs(@GraphQLArgument(name = "band_id") Long bandID) {
        return authorToSongService.getMusicBandSongs(bandID);
    }

}
