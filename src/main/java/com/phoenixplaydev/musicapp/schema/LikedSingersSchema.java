package com.phoenixplaydev.musicapp.schema;

import com.phoenixplaydev.musicapp.configuration.security.RolesChecker;
import com.phoenixplaydev.musicapp.model.tables.pojos.Singer;
import com.phoenixplaydev.musicapp.service.LikedSingersService;
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
public class LikedSingersSchema {

    @Autowired
    private LikedSingersService likedSingersService;

    @Autowired
    private UserService userService;

    @Autowired
    private RolesChecker rolesChecker;

    @GraphQLQuery(name = "getUserLikedSingers")
    public List<Singer> getUserLikedSingers(Long id) {
        rolesChecker.userHasAdminRole();
        return likedSingersService.getLikedSingers(id);
    }

    @GraphQLQuery(name = "getLikedSingers")
    public List<Singer> getLikedSingers() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long requestersID = userService.getUserByEmail(authentication.getName()).getId();
        return likedSingersService.getLikedSingers(requestersID);
    }

    @GraphQLMutation(name = "switchUserLikeSingerStatus")
    public void switchUserLikeSingerStatus(@GraphQLArgument(name = "user_id") Long userID,
                                           @GraphQLArgument(name = "singer_id") Long singerID) {
        rolesChecker.userHasAdminRole();
        likedSingersService.switchLikeSingerStatus(userID, singerID);
    }

    @GraphQLMutation(name = "switchLikedSingerStatus")
    public void switchLikedSingerStatus(@GraphQLArgument(name = "song_id") Long singerID) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long requestersID = userService.getUserByEmail(authentication.getName()).getId();
        likedSingersService.switchLikeSingerStatus(requestersID, singerID);
    }

}
