package com.phoenixplaydev.musicapp.service.Album;

import com.phoenixplaydev.musicapp.model.tables.pojos.Album;
import graphql.kickstart.tools.GraphQLMutationResolver;
import graphql.kickstart.tools.GraphQLQueryResolver;
import io.leangen.graphql.annotations.GraphQLArgument;
import io.leangen.graphql.annotations.GraphQLMutation;
import io.leangen.graphql.annotations.GraphQLQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AlbumResolver {

    @Autowired
    private IAlbumService albumService;

    @Secured("User")
    @GraphQLQuery(name = "getAlbumByID")
    public Album getAlbumByID(@GraphQLArgument(name = "id") Long id) {
        return albumService.getAlbumByID(id);
    }

    @Secured("User")
    @GraphQLQuery(name = "getAllAlbums")
    public List<Album> getAllAlbums() {
        return albumService.getAllAlbums();
    }

    @Secured("Admin")
    @GraphQLMutation(name = "addAlbum")
    public void addAlbum(@GraphQLArgument(name = "name") String name,
                         @GraphQLArgument(name = "cover") byte[] cover) {

        Album album = new Album();
        if(name == null || name.isBlank())
            throw new IllegalArgumentException("Album name can't be null or empty!");
        album.setName(name);
        album.setCover(cover);

        albumService.addAlbum(album);
    }

    @Secured("Admin")
    @GraphQLMutation(name = "updateAlbumInfo")
    public void updateAlbumInfo(@GraphQLArgument(name = "id") Long id,
                                @GraphQLArgument(name = "name") String name,
                                @GraphQLArgument(name = "cover") byte[] cover) {
        Album album = albumService.getAlbumByID(id);
        if(name != null) {
            if (name.isBlank())
                throw new IllegalArgumentException("Album name can't be empty!");
            album.setName(name);
        }
        if(cover != null)
            album.setCover(cover);
        albumService.updateAlbumInfo(album);
    }

    @Secured("Admin")
    @GraphQLMutation(name = "deleteAlbum")
    public void deleteAlbum(@GraphQLArgument(name = "id") Long id) {
        albumService.deleteAlbum(id);
    }

}
