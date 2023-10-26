package com.phoenixplaydev.musicapp.schema;

import com.phoenixplaydev.musicapp.model.tables.pojos.Song;
import com.phoenixplaydev.musicapp.service.RecommendationService;
import com.phoenixplaydev.musicapp.service.UserService;
import io.leangen.graphql.annotations.GraphQLArgument;
import io.leangen.graphql.annotations.GraphQLQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RecommendationSchema {

    @Autowired
    private UserService userService;

    @Autowired
    private RecommendationService recommendationService;

    @GraphQLQuery(name = "getRecommendationsByGenre")
    public List<Song> getRecommendationsByGenre(@GraphQLArgument(name = "amount") int amount) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long requestersID = userService.getUserByEmail(authentication.getName()).getId();
        return recommendationService.getRecommendationsByGenres(requestersID, amount);
    }

    @GraphQLQuery(name = "getRecommendationsByAlbums")
    public List<Song> getRecommendationsByAlbums(@GraphQLArgument(name = "amount") int amount) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long requestersID = userService.getUserByEmail(authentication.getName()).getId();
        return recommendationService.getRecommendationsByAlbums(requestersID, amount);
    }

}
