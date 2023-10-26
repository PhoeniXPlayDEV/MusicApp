package com.phoenixplaydev.musicapp.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.phoenixplaydev.musicapp.response.ErrorResponse;
import com.phoenixplaydev.musicapp.exceptions.GraphQLExceptionHandler;
import com.phoenixplaydev.musicapp.schema.*;
import graphql.ExecutionInput;
import graphql.ExecutionResult;
import graphql.GraphQL;
import graphql.GraphQLException;
import io.leangen.graphql.GraphQLSchemaGenerator;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Pattern;

@RestController
public class GraphQLController {

    private final GraphQL graphQLMain;
    private final GraphQL authGraphQL;

    @Autowired
    public GraphQLController(MusicBandSchema musicBandSchema,
                             SingerSchema singerSchema,
                             AlbumSchema albumSchema,
                             SongSchema songSchema,
                             UserSchema userSchema,
                             AuthSchema authSchema,
                             AuthorToSongSchema authorToSongSchema,
                             AuthorToAlbumSchema authorToAlbumSchema,
                             LikedMusicBandSchema likedMusicBandSchema,
                             LikedSingersSchema likedSingersSchema,
                             LikedAlbumsSchema likedAlbumsSchema,
                             LikedSongsSchema likedSongsSchema,
                             RecommendationSchema recommendationSchema,
                             GraphQLExceptionHandler exceptionHandler
    ) {

        var mainGraphQLSchema = new GraphQLSchemaGenerator()
                .withBasePackages("com.phoenixplaydev.musicapp.service")
                .withOperationsFromSingleton(musicBandSchema)
                .withOperationsFromSingleton(singerSchema)
                .withOperationsFromSingleton(albumSchema)
                .withOperationsFromSingleton(songSchema)
                .withOperationsFromSingleton(userSchema)
                .withOperationsFromSingleton(authorToSongSchema)
                .withOperationsFromSingleton(authorToAlbumSchema)
                .withOperationsFromSingleton(likedMusicBandSchema)
                .withOperationsFromSingleton(likedSingersSchema)
                .withOperationsFromSingleton(likedAlbumsSchema)
                .withOperationsFromSingleton(likedSongsSchema)
                .withOperationsFromSingleton(recommendationSchema)
                .generate();

        var authGraphQLSchema = new GraphQLSchemaGenerator()
                .withBasePackages("com.phoenixplaydev.musicapp.service")
                .withOperationsFromSingleton(authSchema)
                .generate();

        this.graphQLMain = new GraphQL.Builder(mainGraphQLSchema)
                .defaultDataFetcherExceptionHandler(exceptionHandler)
                .build();
        this.authGraphQL = new GraphQL.Builder(authGraphQLSchema)
                .defaultDataFetcherExceptionHandler(exceptionHandler)
                .build();
    }

    @PostMapping(value = "/graphql")
    public ResponseEntity<String> executeGraphQL(HttpServletRequest request)
            throws GraphQLException, IOException {
        return getResult(graphQLMain, request);
    }

    @PostMapping(value = "/auth")
    public ResponseEntity<String> executeAuthGraphQL(HttpServletRequest request)
            throws GraphQLException {
        return getResult(authGraphQL, request);
    }

    @PostMapping(value = "/graphql_inspect")
    public Map<String, Object> executeGraphQLInspect(@RequestBody Map<String, String> request,
                                                     HttpServletRequest raw) throws GraphQLException {
        CompletableFuture<Map<String, Object>> future = CompletableFuture.supplyAsync(() -> {
            String query = request.get("query");
            if(!isIntrospectionQuery(query))
                return new HashMap<>(){{ put("Error-1", new ErrorResponse("Access denied! Not introspection query!")); }};

            ExecutionResult result = graphQLMain.execute(ExecutionInput.newExecutionInput()
                    .query(query)
                    .build());
            return result.toSpecification();
            }
        );
        return future.join();
    }

    private boolean isIntrospectionQuery(String query){
        Pattern pattern = Pattern.compile("^\\s*query\\s*\\{\\s*__schema\\s*\\{[ \r\t\na-zA-Z{}]*\\}\\s*$");
        return pattern.matcher(query).matches();
    }

    private ResponseEntity<String> getResult(GraphQL graphQL, HttpServletRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CompletableFuture<ResponseEntity<String>> future = CompletableFuture.supplyAsync(() -> {
            SecurityContextHolder.getContext().setAuthentication(authentication);
            try {
                String jsonQuery = readRequest(request);
                return writeResponse(graphQL, jsonQuery);
            } catch (IOException e) {
                return ResponseEntity.internalServerError().body("{\"Error-1\": \"" + e.getMessage() + "\"}");
            }
        });
        return future.join();
    }

    private String readRequest(HttpServletRequest request) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        String requestBody = request.getReader()
                .lines()
                .reduce("",
                        (accumulator, actual) ->
                                accumulator + actual
                );
        return (String) objectMapper.readValue(requestBody, Map.class).get("query");
    }

    private ResponseEntity<String> writeResponse(GraphQL graphQL, String jsonQuery)
            throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        headers.add("X-Content-Type-Options", "nosniff");
        ExecutionResult result = graphQL.execute(jsonQuery);
        if(!result.getErrors().isEmpty()) {
            var errors = new HashMap<String, Object>();
            AtomicInteger i = new AtomicInteger(1);
            result.getErrors().forEach(err -> {
                errors.put("Error-" + i.get(),
                        new ErrorResponse(err.getMessage())
                );
                i.getAndIncrement();
            });
            String jsonResponse = objectMapper.writeValueAsString(errors);
            return ResponseEntity.badRequest().headers(headers).body(jsonResponse);
        }
        String jsonResponse = objectMapper.writeValueAsString(result);
        return ResponseEntity.ok().headers(headers).body(jsonResponse);
    }

}
