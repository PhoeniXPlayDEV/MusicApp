package com.phoenixplaydev.musicapp.schema;

import com.phoenixplaydev.musicapp.configuration.security.RolesChecker;
import com.phoenixplaydev.musicapp.model.tables.pojos.Album;
import com.phoenixplaydev.musicapp.service.AlbumService;
import io.leangen.graphql.annotations.GraphQLArgument;
import io.leangen.graphql.annotations.GraphQLMutation;
import io.leangen.graphql.annotations.GraphQLQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AlbumSchema {

    @Autowired
    private AlbumService albumService;

    @Autowired
    private RolesChecker rolesChecker;

    @GraphQLQuery(name = "getAlbumByID")
    public Album getAlbumByID(@GraphQLArgument(name = "album_id") Long albumID) {
        return albumService.getAlbumByID(albumID);
    }

    @GraphQLQuery(name = "getAllAlbums")
    public List<Album> getAllAlbums() {
        return albumService.getAllAlbums();
    }

    @GraphQLMutation(name = "addAlbum")
    public void addAlbum(@GraphQLArgument(name = "name") String name,
                         @GraphQLArgument(name = "cover") byte[] cover) {
        rolesChecker.userHasAdminRole();
        albumService.addAlbum(name, cover);
    }

    @GraphQLMutation(name = "updateAlbumInfo")
    public void updateAlbumInfo(@GraphQLArgument(name = "album_id") Long albumID,
                                @GraphQLArgument(name = "name") String name,
                                @GraphQLArgument(name = "cover") byte[] cover) {
        rolesChecker.userHasAdminRole();
        albumService.updateAlbumInfo(albumID, name, cover);
    }

    @GraphQLMutation(name = "deleteAlbum")
    public void deleteAlbum(@GraphQLArgument(name = "album_id") Long albumID) {
        rolesChecker.userHasAdminRole();
        albumService.deleteAlbum(albumID);
    }

}
