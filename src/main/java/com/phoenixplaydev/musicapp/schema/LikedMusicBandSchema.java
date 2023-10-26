package com.phoenixplaydev.musicapp.schema;

import com.phoenixplaydev.musicapp.configuration.security.RolesChecker;
import com.phoenixplaydev.musicapp.model.tables.pojos.MusicBand;
import com.phoenixplaydev.musicapp.service.LikedMusicBandsService;
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
public class LikedMusicBandSchema {

    @Autowired
    private LikedMusicBandsService likedMusicBandsService;

    @Autowired
    private UserService userService;

    @Autowired
    private RolesChecker rolesChecker;

    @GraphQLQuery(name = "getUserLikedMusicBands")
    public List<MusicBand> getUserLikedMusicBands(Long id) {
        rolesChecker.userHasAdminRole();
        return likedMusicBandsService.getLikedMusicBands(id);
    }

    @GraphQLQuery(name = "getLikedMusicBands")
    public List<MusicBand> getLikedMusicBands() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long requestersID = userService.getUserByEmail(authentication.getName()).getId();
        return likedMusicBandsService.getLikedMusicBands(requestersID);
    }

    @GraphQLMutation(name = "switchUserLikeMusicBandStatus")
    public void switchUserLikeMusicBandStatus(@GraphQLArgument(name = "user_id") Long userID,
                                              @GraphQLArgument(name = "band_id") Long musicBandID) {
        rolesChecker.userHasAdminRole();
        likedMusicBandsService.switchLikeMusicBandStatus(userID, musicBandID);
    }

    @GraphQLMutation(name = "switchLikedMusicBandStatus")
    public void switchLikedMusicBandStatus(@GraphQLArgument(name = "band_id") Long musicBandID) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long requestersID = userService.getUserByEmail(authentication.getName()).getId();
        likedMusicBandsService.switchLikeMusicBandStatus(requestersID, musicBandID);
    }

}
