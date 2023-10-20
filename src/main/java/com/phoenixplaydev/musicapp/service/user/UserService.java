package com.phoenixplaydev.musicapp.service.user;

import com.phoenixplaydev.musicapp.model.Tables;
import com.phoenixplaydev.musicapp.model.tables.pojos.*;
import org.jooq.DSLContext;
import org.jooq.exception.DataAccessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService implements IUserService {

    @Autowired
    private DSLContext dslContext;

    @Override
    public boolean isUserExists(Long id) {
        return dslContext.selectFrom(Tables.USER)
                .where(Tables.USER.ID.eq(id))
                .fetch().isNotEmpty();
    }

    @Override
    public boolean isUserExists(String email) {
        return dslContext.selectFrom(Tables.USER)
                .where(Tables.USER.EMAIL.eq(email))
                .fetch().isNotEmpty();
    }

    @Override
    public User getUserByEmail(String email) {
        var result = dslContext.selectFrom(Tables.USER)
                .where(Tables.USER.EMAIL.eq(email))
                .fetch();
        if(result.isEmpty())
            return null;
        return result.get(0).into(User.class);
    }

    @Override
    public User getUserByID(Long id) {
        var result = dslContext.selectFrom(Tables.USER)
                .where(Tables.USER.ID.eq(id))
                .fetch();
        if(result.isEmpty())
            return null;
        return result.get(0).into(User.class);
    }

    @Override
    public List<User> getAllUsers() {
        return dslContext.selectFrom(Tables.USER)
                .fetchInto(User.class);
    }

    @Override
    public void addUser(User user) {
        dslContext.insertInto(
                    Tables.USER,
                    Tables.USER.ROLE,
                    Tables.USER.NICKNAME,
                    Tables.USER.EMAIL,
                    Tables.USER.PASSWORD
                ).values(
                    user.getRole(),
                    user.getNickname(),
                    user.getEmail(),
                    user.getPassword()
        ).execute();
    }

    @Override
    public void updateUserInfo(User user) {
        dslContext.update(Tables.USER)
                .set(dslContext.newRecord(Tables.USER, user))
                .where(Tables.USER.ID.eq(user.getId()))
                .returning()
                .fetchOptional()
                .orElseThrow(() ->
                        new DataAccessException(
                                "Can't update user with ID = " +
                                        user.getId()))
                .into(User.class);
    }

    /************************
     * Next are liked Songs *
     ************************/

    @Override
    public List<Song> getLikedSongs(Long id) {
        return dslContext.select()
                .from(Tables.LIKED_SONG)
                .join(Tables.SONG)
                .on(Tables.LIKED_SONG.SONG_ID.eq(Tables.SONG.ID))
                .where(Tables.LIKED_SONG.USER_ID.eq(id))
                .fetchInto(Song.class);
    }

    @Override
    public boolean isSongLiked(Long userID, Long songID) {
        return dslContext.selectFrom(Tables.LIKED_SONG)
                .where(Tables.LIKED_SONG.USER_ID.eq(userID).and(Tables.LIKED_SONG.SONG_ID.eq(songID)))
                .fetch().isNotEmpty();
    }

    @Override
    public void likeSong(Long userID, Long songID) {
        if(isSongLiked(userID, songID))
            throw new DataAccessException("Song with ID = " + songID +
                    " already liked by the user with ID = " + userID + "!");
        dslContext.insertInto(
                Tables.LIKED_SONG,
                Tables.LIKED_SONG.USER_ID,
                Tables.LIKED_SONG.SONG_ID
        ).values(
                userID,
                songID
        ).execute();
    }

    @Override
    public void unlikeSong(Long userID, Long songID) {
        if(!isSongLiked(userID, songID))
            throw new DataAccessException("Song with ID = " + songID +
                    " has not been liked by the user with ID = " + userID + "!");
        dslContext.deleteFrom(Tables.LIKED_SONG)
                .where(Tables.LIKED_SONG.USER_ID.eq(userID).and(Tables.LIKED_SONG.SONG_ID.eq(songID)))
                .execute();
    }

    @Override
    public void switchLikeSongStatus(Long userID, Long songID) {
        if(isSongLiked(userID, songID)) {
            unlikeSong(userID, songID);
            return;
        }
        likeSong(userID, songID);
    }

    /*************************
     * Next are liked Albums *
     *************************/

    @Override
    public List<Album> getLikedAlbums(Long id) {
        return dslContext.select()
                .from(Tables.LIKED_ALBUM)
                .join(Tables.ALBUM)
                .on(Tables.LIKED_ALBUM.ALBUM_ID.eq(Tables.ALBUM.ID))
                .where(Tables.LIKED_ALBUM.USER_ID.eq(id))
                .fetchInto(Album.class);
    }

    @Override
    public boolean isAlbumLiked(Long userID, Long albumID) {
        return dslContext.selectFrom(Tables.LIKED_ALBUM)
                .where(Tables.LIKED_ALBUM.USER_ID.eq(userID).and(Tables.LIKED_ALBUM.ALBUM_ID.eq(albumID)))
                .fetch().isNotEmpty();
    }

    @Override
    public void likeAlbum(Long userID, Long albumID) {
        if(isSongLiked(userID, albumID))
            throw new DataAccessException("Album with ID = " + albumID +
                    " already liked by the user with ID = " + userID + "!");
        dslContext.insertInto(
                Tables.LIKED_ALBUM,
                Tables.LIKED_ALBUM.USER_ID,
                Tables.LIKED_ALBUM.ALBUM_ID
        ).values(
                userID,
                albumID
        ).execute();
    }

    @Override
    public void unlikeAlbum(Long userID, Long albumID) {
        if(!isSongLiked(userID, albumID))
            throw new DataAccessException("Album with ID = " + albumID +
                    " has not been liked by the user with ID = " + userID + "!");
        dslContext.deleteFrom(Tables.LIKED_ALBUM)
                .where(Tables.LIKED_ALBUM.USER_ID.eq(userID).and(Tables.LIKED_ALBUM.ALBUM_ID.eq(albumID)))
                .execute();
    }

    @Override
    public void switchLikeAlbumStatus(Long userID, Long albumID) {
        if(isAlbumLiked(userID, albumID)) {
            unlikeAlbum(userID, albumID);
            return;
        }
        likeAlbum(userID, albumID);
    }

    /**************************
     * Next are liked Singers *
     **************************/

    @Override
    public List<Singer> getLikedSingers(Long id) {
        return dslContext.select()
                .from(Tables.LIKED_SINGER)
                .join(Tables.SINGER)
                .on(Tables.LIKED_SINGER.SINGER_ID.eq(Tables.SINGER.ID))
                .where(Tables.LIKED_SINGER.USER_ID.eq(id))
                .fetchInto(Singer.class);
    }

    @Override
    public boolean isSingerLiked(Long userID, Long singerID) {
        return dslContext.selectFrom(Tables.LIKED_SINGER)
                .where(Tables.LIKED_SINGER.USER_ID.eq(userID).and(Tables.LIKED_SINGER.SINGER_ID.eq(singerID)))
                .fetch().isNotEmpty();
    }

    @Override
    public void likeSinger(Long userID, Long singerID) {
        if(isSingerLiked(userID, singerID))
            throw new DataAccessException("Singer with ID = " + singerID +
                    " already liked by the user with ID = " + userID + "!");
        dslContext.insertInto(
                Tables.LIKED_SINGER,
                Tables.LIKED_SINGER.USER_ID,
                Tables.LIKED_SINGER.SINGER_ID
        ).values(
                userID,
                singerID
        ).execute();
    }

    @Override
    public void unlikeSinger(Long userID, Long singerID) {
        if(!isSingerLiked(userID, singerID))
            throw new DataAccessException("Singer with ID = " + singerID +
                    " has not been liked by the user with ID = " + userID + "!");
        dslContext.deleteFrom(Tables.LIKED_SINGER)
                .where(Tables.LIKED_SINGER.USER_ID.eq(userID).and(Tables.LIKED_SINGER.SINGER_ID.eq(singerID)))
                .execute();
    }

    @Override
    public void switchLikeSingerStatus(Long userID, Long singerID) {
        if(isSingerLiked(userID, singerID)) {
            unlikeSinger(userID, singerID);
            return;
        }
        likeSinger(userID, singerID);
    }

    /******************************
     * Next are liked Music Bands *
     ******************************/

    @Override
    public List<MusicBand> getLikedMusicBands(Long id) {
        return dslContext.select()
                .from(Tables.LIKED_BAND)
                .join(Tables.MUSIC_BAND)
                .on(Tables.LIKED_BAND.BAND_ID.eq(Tables.MUSIC_BAND.ID))
                .where(Tables.LIKED_BAND.USER_ID.eq(id))
                .fetchInto(MusicBand.class);
    }

    @Override
    public boolean isMusicBandLiked(Long userID, Long musicBandID) {
        return dslContext.selectFrom(Tables.LIKED_BAND)
                .where(Tables.LIKED_BAND.USER_ID.eq(userID).and(Tables.LIKED_BAND.BAND_ID.eq(musicBandID)))
                .fetch().isNotEmpty();
    }

    @Override
    public void likeMusicBand(Long userID, Long musicBandID) {
        if(isMusicBandLiked(userID, musicBandID))
            throw new DataAccessException("Music band with ID = " + musicBandID +
                    " already liked by the user with ID = " + userID + "!");
        dslContext.insertInto(
                Tables.LIKED_BAND,
                Tables.LIKED_BAND.USER_ID,
                Tables.LIKED_BAND.BAND_ID
        ).values(
                userID,
                musicBandID
        ).execute();
    }

    @Override
    public void unlikeMusicBand(Long userID, Long musicBandID) {
        if(!isMusicBandLiked(userID, musicBandID))
            throw new DataAccessException("Music band with ID = " + musicBandID +
                    " has not been liked by the user with ID = " + userID + "!");
        dslContext.deleteFrom(Tables.LIKED_BAND)
                .where(Tables.LIKED_BAND.USER_ID.eq(userID).and(Tables.LIKED_BAND.BAND_ID.eq(musicBandID)))
                .execute();
    }

    @Override
    public void switchLikeMusicBandStatus(Long userID, Long musicBandID) {
        if(isMusicBandLiked(userID, musicBandID)) {
            unlikeMusicBand(userID, musicBandID);
            return;
        }
        likeMusicBand(userID, musicBandID);
    }

    @Override
    public void deleteUser(Long id) {
        dslContext.deleteFrom(Tables.USER)
                .where(Tables.USER.ID.eq(id)).execute();
        //TODO: Check that all data about user will be deleted including LIKED_SONG table
    }

}
