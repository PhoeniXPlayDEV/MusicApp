package com.phoenixplaydev.musicapp.schema;

import com.phoenixplaydev.musicapp.configuration.security.RolesChecker;
import com.phoenixplaydev.musicapp.model.tables.pojos.Song;
import com.phoenixplaydev.musicapp.service.LikedSongsService;
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
public class LikedSongsSchema {

    @Autowired
    private LikedSongsService likedSongsService;

    @Autowired
    private UserService userService;

    @Autowired
    private RolesChecker rolesChecker;

    @GraphQLQuery(name = "getUserLikedSongs")
    public List<Song> getUserLikedSongs(@GraphQLArgument(name = "user_id") Long userID) {
        rolesChecker.userHasAdminRole();
        return likedSongsService.getLikedSongs(userID);
    }

    @GraphQLQuery(name = "getLikedSongs")
    public List<Song> getLikedSongs() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long requestersID = userService.getUserByEmail(authentication.getName()).getId();
        return likedSongsService.getLikedSongs(requestersID);
    }

    @GraphQLMutation(name = "switchUserLikeSongStatus")
    public void switchUserLikeSongStatus(@GraphQLArgument(name = "user_id") Long userID,
                                         @GraphQLArgument(name = "song_id") Long songID) {
        rolesChecker.userHasAdminRole();
        likedSongsService.switchLikeSongStatus(userID, songID);
    }

    @GraphQLMutation(name = "switchLikedSongStatus")
    public void switchLikedSongStatus(@GraphQLArgument(name = "song_id") Long songID) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long requestersID = userService.getUserByEmail(authentication.getName()).getId();
        likedSongsService.switchLikeSongStatus(requestersID, songID);
    }

}
