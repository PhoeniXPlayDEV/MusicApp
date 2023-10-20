package com.phoenixplaydev.musicapp.service.song;

import com.phoenixplaydev.musicapp.model.enums.Genre;
import com.phoenixplaydev.musicapp.model.tables.pojos.Song;
import com.phoenixplaydev.musicapp.service.Album.IAlbumService;
import graphql.kickstart.tools.GraphQLMutationResolver;
import graphql.kickstart.tools.GraphQLQueryResolver;
import io.leangen.graphql.annotations.GraphQLArgument;
import io.leangen.graphql.annotations.GraphQLMutation;
import io.leangen.graphql.annotations.GraphQLQuery;
import lombok.SneakyThrows;
import org.jooq.exception.DataAccessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class SongResolver {

    @Autowired
    ISongService songService;
    @Autowired
    IAlbumService albumService;

    @Secured("User")
    @GraphQLQuery(name = "getSongByID")
    public Song getSongByID(@GraphQLArgument(name = "id") Long id) {
        return songService.getSongByID(id);
    }

    @Secured("User")
    @GraphQLQuery(name = "getAllSongs")
    public List<Song> getAllSongs() {
        return songService.getAllSongs();
    }

    @SneakyThrows
    @Secured("Admin")
    @GraphQLMutation(name = "addSong")
    public void addSong(@GraphQLArgument(name = "name") String name,
                        @GraphQLArgument(name = "cover") byte[] cover,
                        @GraphQLArgument(name = "length") double length,
                        @GraphQLArgument(name = "genre") String genre,
                        @GraphQLArgument(name = "file") byte[] file,
                        @GraphQLArgument(name = "album_id") Long album_id) {

        Song song = new Song();
        if(name == null || name.isBlank())
            throw new IllegalArgumentException("Song name can't be null or empty!");
        song.setName(name);
        song.setCover(cover);
        if(length <= 0)
            throw new IllegalArgumentException("Song duration can't be less or equals zero!");
        song.setLength(length);
        if(genre == null || !Arrays.stream(Genre.values()).map(Genre::toString).toList().contains(genre))
            throw new IllegalArgumentException("Can't find genre = '" + genre + "'!");
        song.setGenre(Genre.valueOf(genre));
        if(file == null || file.length == 0)
            throw new IllegalArgumentException("File can't null or empty!");
        song.setFile(file);
        if(!albumService.isAlbumExisted(album_id))
            throw new DataAccessException("Can't add song to not existing album with ID = " + album_id);
        song.setAlbumId(album_id);

        songService.addSong(song);
    }

//    @GraphQLMutation(name = "upload")
//    public String upload(@GraphQLInputField(name = "file") MultipartFile file)  {
//        log.info("File uploaded!");
//        return "File uploaded successfully: ";
//    }

}
