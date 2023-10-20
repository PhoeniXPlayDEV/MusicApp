package com.phoenixplaydev.musicapp.service.singer;

import com.phoenixplaydev.musicapp.model.tables.pojos.Singer;
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
public class SingerResolver implements GraphQLQueryResolver, GraphQLMutationResolver {

    @Autowired
    private ISingerService singerService;

    @Secured("User")
    @GraphQLQuery(name = "getSingerByID")
    public Long getSingerByID(@GraphQLArgument(name = "id") java.lang.Long id) {
        return singerService.getSingerByID(id);
    }

    @Secured("User")
    @GraphQLQuery(name = "getAllSingers")
    public List<Long> getAllSingers() {
        return singerService.getAllSingers();
    }

    @Secured("Admin")
    @GraphQLMutation(name = "AddSinger")
    public void addSinger(@GraphQLArgument(name = "name") String name,
                          @GraphQLArgument(name = "cover") byte[] cover) {

        Singer singer = new Singer();
        if (name == null || name.isBlank())
            throw new IllegalArgumentException("Siner name can't be null or empty!");
        singer.setName(name);
        singer.setCover(cover);

        singerService.addSinger(singer);
    }

}
