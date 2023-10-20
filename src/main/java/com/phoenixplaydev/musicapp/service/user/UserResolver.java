package com.phoenixplaydev.musicapp.service.user;

import com.phoenixplaydev.musicapp.model.enums.Genre;
import com.phoenixplaydev.musicapp.model.enums.UserRole;
import com.phoenixplaydev.musicapp.model.tables.pojos.*;
import graphql.GraphQLException;
import graphql.kickstart.tools.GraphQLMutationResolver;
import graphql.kickstart.tools.GraphQLQueryResolver;
import io.leangen.graphql.annotations.GraphQLArgument;
import io.leangen.graphql.annotations.GraphQLMutation;
import io.leangen.graphql.annotations.GraphQLQuery;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class UserResolver {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private IUserService userService;

    @Secured("Admin")
    @GraphQLQuery(name = "getUserByID")
    public User getUserByID(@GraphQLArgument(name = "id") Long id) {
        User user = userService.getUserByID(id);
        if(user == null)
            throw new GraphQLException("User with ID = " + id + " not found!");
        return user;
    }

    @Secured("Admin")
    @GraphQLQuery(name = "getAllUsers")
    public List<User> getAllUsers() throws Exception {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication.getAuthorities().stream().noneMatch(a -> a.getAuthority().equals("Admin")))
            throw new AccessDeniedException("Access denied!");
        return userService.getAllUsers();
    }

    @Secured("Admin")
    @GraphQLMutation(name = "addUser")
    public void addUser(@GraphQLArgument(name = "role") String role,
                        @GraphQLArgument(name = "nickname") String nickname,
                        @GraphQLArgument(name = "email") String email,
                        @GraphQLArgument(name = "password") String password) {
        User user = new User();
        user.setRole(UserRole.valueOf(role));
        user.setNickname(nickname);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        userService.addUser(user);
    }

    @Secured("Admin")
    @GraphQLMutation(name = "updateUserInfo")
    public void updateUserInfo(@NonNull @GraphQLArgument(name = "id") Long id,
                               @GraphQLArgument(name = "role") String role,
                               @GraphQLArgument(name = "nickname") String nickname,
                               @GraphQLArgument(name = "email") String email,
                               @GraphQLArgument(name = "password") String password
    ) throws Exception {

        User user = userService.getUserByID(id);

        if(role != null &&
                Arrays.stream(UserRole.values()).
                        map(UserRole::getName).toList().contains(role))
            user.setRole(UserRole.valueOf(role));
        if(nickname != null && !nickname.isBlank())
            user.setNickname(nickname);
        if(email != null && !email.isBlank()) {}
            user.setEmail(email);
        if(password != null && !password.isBlank())
            user.setPassword(passwordEncoder.encode(password));

        userService.updateUserInfo(user);
    }

    /************************
     * Next are liked Songs *
     ************************/

    @GraphQLQuery(name = "getUserLikedSongs")
    public List<Song> getUserLikedSongs(@GraphQLArgument(name = "id") Long id) {
        return userService.getLikedSongs(id);
    }

    @Secured("User")
    @GraphQLQuery(name = "getLikedSongs")
    public List<Song> getLikedSongs() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long requestersID = userService.getUserByEmail(authentication.getName()).getId();
        return userService.getLikedSongs(requestersID);
    }

    @Secured("Admin")
    @GraphQLMutation(name = "switchUserLikeSongStatus")
    public void switchUserLikeSongStatus(@GraphQLArgument(name = "user_id") Long userID,
                                         @GraphQLArgument(name = "song_id") Long songID) {
        userService.switchLikeSongStatus(userID, songID);
    }

    @Secured("User")
    @GraphQLMutation(name = "switchLikedSongStatus")
    public void switchLikedSongStatus(@GraphQLArgument(name = "song_id") Long songID) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long requestersID = userService.getUserByEmail(authentication.getName()).getId();
        userService.switchLikeSongStatus(requestersID, songID);
    }

    /*************************
     * Next are liked Albums *
     *************************/

    @Secured("Admin")
    @GraphQLQuery(name = "getUserLikedAlbums")
    public List<Album> getUserLikedAlbums(@GraphQLArgument(name = "id") Long id) {
        return userService.getLikedAlbums(id);
    }

    @Secured("User")
    @GraphQLQuery(name = "getLikedAlbums")
    public List<Album> getLikedAlbums() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long requestersID = userService.getUserByEmail(authentication.getName()).getId();
        return userService.getLikedAlbums(requestersID);
    }

    @Secured("Admin")
    @GraphQLMutation(name = "switchUserLikeAlbumStatus")
    public void switchUserLikeAlbumStatus(@GraphQLArgument(name = "user_id") Long userID,
                                         @GraphQLArgument(name = "album_id") Long albumID) {
        userService.switchLikeAlbumStatus(userID, albumID);
    }

    @Secured("User")
    @GraphQLMutation(name = "switchLikedAlbumStatus")
    public void switchLikedAlbumStatus(@GraphQLArgument(name = "album_id") Long albumID) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long requestersID = userService.getUserByEmail(authentication.getName()).getId();
        userService.switchLikeAlbumStatus(requestersID, albumID);
    }

    /**************************
     * Next are liked Singers *
     **************************/

    @Secured("Admin")
    @GraphQLQuery(name = "getUserLikedSingers")
    public List<Singer> getUserLikedSingers(Long id) {
        return userService.getLikedSingers(id);
    }

    @Secured("User")
    @GraphQLQuery(name = "getLikedSingers")
    public List<Singer> getLikedSingers() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long requestersID = userService.getUserByEmail(authentication.getName()).getId();
        return userService.getLikedSingers(requestersID);
    }

    @Secured("Admin")
    @GraphQLMutation(name = "switchUserLikeSingerStatus")
    public void switchUserLikeSingerStatus(@GraphQLArgument(name = "user_id") Long userID,
                                         @GraphQLArgument(name = "singer_id") Long singerID) {
        userService.switchLikeSingerStatus(userID, singerID);
    }

    @Secured("User")
    @GraphQLMutation(name = "switchLikedSingerStatus")
    public void switchLikedSingerStatus(@GraphQLArgument(name = "song_id") Long singerID) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long requestersID = userService.getUserByEmail(authentication.getName()).getId();
        userService.switchLikeSingerStatus(requestersID, singerID);
    }

    /******************************
     * Next are liked Music Bands *
     ******************************/

    @Secured("Admin")
    @GraphQLQuery(name = "getUserLikedMusicBands")
    public List<MusicBand> getUserLikedMusicBands(Long id) {
        return userService.getLikedMusicBands(id);
    }

    @Secured("User")
    @GraphQLQuery(name = "getLikedMusicBands")
    public List<MusicBand> getLikedMusicBands() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long requestersID = userService.getUserByEmail(authentication.getName()).getId();
        return userService.getLikedMusicBands(requestersID);
    }

    @Secured("Admin")
    @GraphQLMutation(name = "switchUserLikeMusicBandStatus")
    public void switchUserLikeMusicBandStatus(@GraphQLArgument(name = "user_id") Long userID,
                                              @GraphQLArgument(name = "band_id") Long musicBandID) {
        userService.switchLikeMusicBandStatus(userID, musicBandID);
    }

    @Secured("User")
    @GraphQLMutation(name = "switchLikedMusicBandStatus")
    public void switchLikedMusicBandStatus(@GraphQLArgument(name = "band_id") Long musicBandID) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long requestersID = userService.getUserByEmail(authentication.getName()).getId();
        userService.switchLikeMusicBandStatus(requestersID, musicBandID);
    }

    @Secured("User")
    @GraphQLMutation(name = "updatePassword")
    public void updatePassword(@GraphQLArgument(name = "password") String password)
            throws Exception {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long requestersID = userService.getUserByEmail(authentication.getName()).getId();
        User user = userService.getUserByID(requestersID);
        user.setPassword(passwordEncoder.encode(password));
        userService.updateUserInfo(user);
    }

    @Secured("Admin")
    @GraphQLMutation(name = "deleteUser")
    public void deleteUser(@GraphQLArgument(name = "id") Long id){
        userService.deleteUser(id);
    }

    @Secured("Admin")
    @GraphQLQuery(name = "getUsersMostLikedGenres")
    public List<Map.Entry<Genre, Long>> getUsersMostLikedGenres(Long id) {
        return getUserLikedSongs(id).stream()
                .collect(Collectors.groupingBy(Song::getGenre, Collectors.counting()))
                .entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue())
                .toList();
    }

}
