package com.phoenixplaydev.musicapp.schema;

import com.phoenixplaydev.musicapp.configuration.security.RolesChecker;
import com.phoenixplaydev.musicapp.model.tables.pojos.Singer;
import com.phoenixplaydev.musicapp.service.SingerService;
import io.leangen.graphql.annotations.GraphQLArgument;
import io.leangen.graphql.annotations.GraphQLMutation;
import io.leangen.graphql.annotations.GraphQLQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SingerSchema {

    @Autowired
    private SingerService singerService;

    @Autowired
    private RolesChecker rolesChecker;

    @GraphQLQuery(name = "getSingerByID")
    public Singer getSingerByID(@GraphQLArgument(name = "singer_id") Long singerID) {
        return singerService.getSingerByID(singerID);
    }

    @GraphQLQuery(name = "getAllSingers")
    public List<Singer> getAllSingers() {
        return singerService.getAllSingers();
    }

    @GraphQLMutation(name = "updateSingerInfo")
    public void updateSingerInfo(@GraphQLArgument(name = "singer_id") Long singerID,
                                 @GraphQLArgument(name = "name") String name,
                                 @GraphQLArgument(name = "cover") byte[] cover) {
        rolesChecker.userHasAdminRole();
        singerService.updateSingerInfo(singerID, name, cover);
    }

    @GraphQLMutation(name = "addSinger")
    public void addSinger(@GraphQLArgument(name = "name") String name,
                          @GraphQLArgument(name = "cover") byte[] cover) {
        rolesChecker.userHasAdminRole();
        singerService.addSinger(name, cover);
    }

    @GraphQLMutation(name = "deleteSinger")
    public void deleteSinger(@GraphQLArgument(name = "singer_id") Long singerID) {
        singerService.deleteSinger(singerID);
    }

}
