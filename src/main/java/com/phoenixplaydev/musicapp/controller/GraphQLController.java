package com.phoenixplaydev.musicapp.controller;

import com.phoenixplaydev.musicapp.exceptions.ErrorResponse;
import com.phoenixplaydev.musicapp.exceptions.GraphQLExceptionHandler;
import com.phoenixplaydev.musicapp.service.Album.AlbumResolver;
import com.phoenixplaydev.musicapp.service.musicband.MusicBandResolver;
import com.phoenixplaydev.musicapp.service.singer.SingerResolver;
import com.phoenixplaydev.musicapp.service.song.SongResolver;
import com.phoenixplaydev.musicapp.service.user.UserResolver;
import com.phoenixplaydev.musicapp.service.authentication.AuthService;
import graphql.ExecutionResult;
import graphql.GraphQL;
import graphql.GraphQLException;
import io.leangen.graphql.GraphQLSchemaGenerator;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@RestController
public class GraphQLController {

    private final GraphQL graphQL;
    private final GraphQL authGraphQL;

    @Autowired
    public GraphQLController(MusicBandResolver musicBandResolver,
                             SingerResolver singerResolver,
                             AlbumResolver albumResolver,
                             SongResolver songResolver,
                             UserResolver userResolver,
                             AuthService authService,
                             GraphQLExceptionHandler exceptionHandler
    ) {

        var schema = new GraphQLSchemaGenerator()
                .withBasePackages("com.phoenixplaydev.musicapp.service")
                .withOperationsFromSingleton(musicBandResolver)
                .withOperationsFromSingleton(singerResolver)
                .withOperationsFromSingleton(albumResolver)
                .withOperationsFromSingleton(songResolver)
                .withOperationsFromSingleton(userResolver)
                .generate();

        var authSchema = new GraphQLSchemaGenerator()
                .withBasePackages("com.phoenixplaydev.musicapp.service")
                .withOperationsFromSingleton(authService)
                .generate();

        this.graphQL = new GraphQL.Builder(schema)
                .defaultDataFetcherExceptionHandler(exceptionHandler)
                .build();
        this.authGraphQL = new GraphQL.Builder(authSchema)
                .defaultDataFetcherExceptionHandler(exceptionHandler)
                .build();
    }

    @PostMapping(value = "/graphql")
    public Map<String, Object> executeGraphQL(@RequestBody Map<String, String> request, HttpServletRequest raw)
            throws GraphQLException {
        return getResult(graphQL.execute(request.get("query")));
    }

    @PostMapping(value = "/auth")
    public Map<String, Object> executeAuthGraphQL(@RequestBody Map<String, String> request, HttpServletRequest raw)
            throws GraphQLException {
        return getResult(authGraphQL.execute(request.get("query")));
    }

    private Map<String, Object> getResult(ExecutionResult result) {
        if(!result.getErrors().isEmpty()) {
            var errors = new HashMap<String, Object>();
            AtomicInteger i = new AtomicInteger(1);
            result.getErrors().forEach(err -> {
                errors.put("Error-" + i.get(),
                        new ErrorResponse(err.getMessage())
                );
                i.getAndIncrement();
            });
            return errors;
        }
        return result.getData();
    }

}
