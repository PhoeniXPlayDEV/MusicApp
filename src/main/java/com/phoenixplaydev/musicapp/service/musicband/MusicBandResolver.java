package com.phoenixplaydev.musicapp.service.musicband;

import com.phoenixplaydev.musicapp.model.tables.pojos.MusicBand;
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
public class MusicBandResolver {

    @Autowired
    private IMusicBandService musicBandService;

    @Secured("User")
    @GraphQLQuery(name = "getMusicBandByID")
    public MusicBand getMusicBandByID(@GraphQLArgument(name = "id") Long id) {
        return musicBandService.getMusicBandByID(id);
    }

    @Secured("User")
    @GraphQLQuery(name = "getAllMusicBands")
    public List<MusicBand> getAllMusicBands() {
        return musicBandService.getAllMusicBands();
    }

    @Secured("Admin")
    @GraphQLMutation(name = "addMusicBand")
    public void addMusicBand(@GraphQLArgument(name = "name") String name,
                             @GraphQLArgument(name = "cover") byte[] cover) {
        if(name == null || name.isBlank())
            throw new IllegalArgumentException("Music band name can't be null or empty!");
        MusicBand musicBand = new MusicBand();
        musicBand.setName(name);
        musicBand.setCover(cover);
        musicBandService.addMusicBand(musicBand);
    }

    @Secured("Admin")
    @GraphQLMutation(name = "updateMusicBandInfo")
    public void updateMusicBandInfo(@GraphQLArgument(name = "id") Long id,
                                @GraphQLArgument(name = "name") String name,
                                @GraphQLArgument(name = "cover") byte[] cover) {
        MusicBand musicBand = getMusicBandByID(id);
        if(name != null) {
            if(name.isBlank())
                throw new IllegalArgumentException("Music band name can't be empty!");
            musicBand.setName(name);
        }
        if(cover != null)
            musicBand.setCover(cover);
        musicBandService.updateMusicBandInfo(musicBand);
    }

    @Secured("Admin")
    @GraphQLMutation(name = "deleteMusicBand")
    public void deleteMusicBand(@GraphQLArgument(name = "id") Long id) {
        musicBandService.deleteMusicBand(id);
    }

}
