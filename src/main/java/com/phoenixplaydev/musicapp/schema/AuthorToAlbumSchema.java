package com.phoenixplaydev.musicapp.schema;

import com.phoenixplaydev.musicapp.configuration.security.RolesChecker;
import com.phoenixplaydev.musicapp.model.tables.pojos.Album;
import com.phoenixplaydev.musicapp.model.tables.pojos.AuthorToAlbum;
import com.phoenixplaydev.musicapp.service.AuthorToAlbumService;
import io.leangen.graphql.annotations.GraphQLArgument;
import io.leangen.graphql.annotations.GraphQLMutation;
import io.leangen.graphql.annotations.GraphQLQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuthorToAlbumSchema {

    @Autowired
    private AuthorToAlbumService authorToAlbumService;

    @Autowired
    private RolesChecker rolesChecker;

    @GraphQLQuery(name = "getAllAlbumAuthors")
    public List<AuthorToAlbum> getAllAlbumAuthors(@GraphQLArgument(name = "album_id") Long albumID) {
        return authorToAlbumService.getAllAlbumAuthors(albumID);
    }

    @GraphQLMutation(name = "addAlbumAuthor")
    public void addAlbumAuthor(@GraphQLArgument(name = "album_id") Long albumID,
                               @GraphQLArgument(name = "singer_id") Long singerID,
                               @GraphQLArgument(name = "band_id") Long bandID) {
        rolesChecker.userHasAdminRole();
        authorToAlbumService.addAlbumAuthor(albumID, singerID, bandID);
    }

    @GraphQLMutation(name = "deleteAlbumAuthor")
    public void deleteAlbumAuthor(@GraphQLArgument(name = "album_id") Long albumID,
                                 @GraphQLArgument(name = "singer_id") Long singerID,
                                 @GraphQLArgument(name = "band_id") Long bandID) {
        rolesChecker.userHasAdminRole();
        authorToAlbumService.deleteAlbumAuthor(albumID, singerID, bandID);
    }

    @GraphQLQuery(name = "getSingerAlbums")
    public List<Album> getSingerAlbums(@GraphQLArgument(name = "singer_id") Long singerID) {
        return authorToAlbumService.getSingerAlbums(singerID);
    }

    @GraphQLQuery(name = "getMusicBandAlbums")
    public List<Album> getMusicBandAlbums(@GraphQLArgument(name = "band_id") Long bandID) {
        return authorToAlbumService.getMusicBandAlbums(bandID);
    }

}
