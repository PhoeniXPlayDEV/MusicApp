package com.phoenixplaydev.musicapp.schema;

import com.phoenixplaydev.musicapp.configuration.security.RolesChecker;
import com.phoenixplaydev.musicapp.model.tables.pojos.MusicBand;
import com.phoenixplaydev.musicapp.service.MusicBandService;
import io.leangen.graphql.annotations.GraphQLArgument;
import io.leangen.graphql.annotations.GraphQLMutation;
import io.leangen.graphql.annotations.GraphQLQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MusicBandSchema {

    @Autowired
    private MusicBandService musicBandService;

    @Autowired
    private RolesChecker rolesChecker;

    @GraphQLQuery(name = "getMusicBandByID")
    public MusicBand getMusicBandByID(@GraphQLArgument(name = "band_id") Long bandID) {
        return musicBandService.getMusicBandByID(bandID);
    }

    @GraphQLQuery(name = "getAllMusicBands")
    public List<MusicBand> getAllMusicBands() {
        return musicBandService.getAllMusicBands();
    }

    @GraphQLMutation(name = "addMusicBand")
    public void addMusicBand(@GraphQLArgument(name = "name") String name,
                             @GraphQLArgument(name = "cover") byte[] cover) {
        rolesChecker.userHasAdminRole();
        musicBandService.addMusicBand(name, cover);
    }

    @GraphQLMutation(name = "updateMusicBandInfo")
    public void updateMusicBandInfo(@GraphQLArgument(name = "band_id") Long bandID,
                                    @GraphQLArgument(name = "name") String name,
                                    @GraphQLArgument(name = "cover") byte[] cover) {
        rolesChecker.userHasAdminRole();
        musicBandService.updateMusicBandInfo(bandID, name, cover);
    }

    @GraphQLMutation(name = "deleteMusicBand")
    public void deleteMusicBand(@GraphQLArgument(name = "band_id") Long bandID) {
        rolesChecker.userHasAdminRole();
        musicBandService.deleteMusicBand(bandID);
    }

}
