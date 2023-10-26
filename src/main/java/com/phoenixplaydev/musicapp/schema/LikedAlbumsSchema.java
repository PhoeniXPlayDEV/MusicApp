package com.phoenixplaydev.musicapp.schema;

import com.phoenixplaydev.musicapp.configuration.security.RolesChecker;
import com.phoenixplaydev.musicapp.model.tables.pojos.Album;
import com.phoenixplaydev.musicapp.service.LikedAlbumsService;
import com.phoenixplaydev.musicapp.service.UserService;
import io.leangen.graphql.annotations.GraphQLArgument;
import io.leangen.graphql.annotations.GraphQLMutation;
import io.leangen.graphql.annotations.GraphQLQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LikedAlbumsSchema {

    @Autowired
    private LikedAlbumsService likedAlbumsService;

    @Autowired
    private UserService userService;

    @Autowired
    private RolesChecker rolesChecker;

    @GraphQLQuery(name = "getUserLikedAlbums")
    public List<Album> getUserLikedAlbums(@GraphQLArgument(name = "user_id") Long userID) {
        rolesChecker.userHasAdminRole();
        return likedAlbumsService.getLikedAlbums(userID);
    }

    @GraphQLQuery(name = "getLikedAlbums")
    public List<Album> getLikedAlbums() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long requestersID = userService.getUserByEmail(authentication.getName()).getId();
        return likedAlbumsService.getLikedAlbums(requestersID);
    }

    @GraphQLMutation(name = "switchUserLikedAlbumStatus")
    public void switchUserLikeAlbumStatus(@GraphQLArgument(name = "user_id") Long userID,
                                          @GraphQLArgument(name = "album_id") Long albumID) {
        rolesChecker.userHasAdminRole();
        likedAlbumsService.switchLikeAlbumStatus(userID, albumID);
    }

    @GraphQLMutation(name = "switchLikedAlbumStatus")
    public void switchLikedAlbumStatus(@GraphQLArgument(name = "album_id") Long albumID) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long requestersID = userService.getUserByEmail(authentication.getName()).getId();
        likedAlbumsService.switchLikeAlbumStatus(requestersID, albumID);
    }

}
