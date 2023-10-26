package com.phoenixplaydev.musicapp.service;

import com.phoenixplaydev.musicapp.model.enums.Genre;
import com.phoenixplaydev.musicapp.model.tables.pojos.Song;
import com.phoenixplaydev.musicapp.repository.LikedSongsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class RecommendationService {

    @Autowired
    private SongService songService;

    @Autowired
    private LikedSongsService likedSongsService;

    @Autowired
    private LikedSongsRepository likedSongsRepository;

    public List<Map.Entry<Genre, Long>> getUserLikedGenres(Long userID) {
        return likedSongsService.getLikedSongs(userID).stream()
                .collect(Collectors.groupingBy(Song::getGenre, Collectors.counting()))
                .entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue())
                .toList();
    }

    public List<Map.Entry<Long, Long>> getUserLikedAlbums(Long userID) {
        return likedSongsService.getLikedSongs(userID).stream()
                .collect(Collectors.groupingBy(Song::getAlbumId, Collectors.counting()))
                .entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue())
                .toList();
    }

    private List<Song> getRecommendationsForEmptyLikedList(int amount) {
        var recommendation = songService.getAllSongs();
        Collections.shuffle(recommendation);
        return recommendation.stream().limit(amount).toList();
    }

    private void fillLeftSongsByRandom(Long userID, int amount, List<Song> recommendation) {
        var left = amount - recommendation.size();
        if(left > 0) {
            var songs = likedSongsRepository.getSongsNotLikedByUser(userID, recommendation);
            Collections.shuffle(songs);
            recommendation.addAll(songs.stream().limit(left).toList());
        }
    }

    public List<Song> getRecommendationsByGenres(Long userID, int amount) {
        var likedGenres = getUserLikedGenres(userID);
        var amountOfLikedSongs = likedSongsService.getAmountOfLikedSongs(userID);
        if(amountOfLikedSongs == 0)
            return getRecommendationsForEmptyLikedList(amount);
        var recommendation = new ArrayList<Song>(amount);
        for(var e : likedGenres) {
            var genre = e.getKey();
            var amountForGenre = e.getValue() * amount / amountOfLikedSongs;
            var songs = likedSongsRepository.getSongsByGenreNotLikedByUser(userID, genre);
            Collections.shuffle(songs);
            recommendation.addAll(songs.stream().limit(amountForGenre).toList());
        }
        fillLeftSongsByRandom(userID, amount, recommendation);
        return recommendation;
    }

    public List<Song> getRecommendationsByAlbums(Long userID, int amount) {
        var likedAlbums = getUserLikedAlbums(userID);
        var amountOfLikedSongs = likedSongsService.getAmountOfLikedSongs(userID);
        if(amountOfLikedSongs == 0)
            return getRecommendationsForEmptyLikedList(amount);
        var recommendation = new ArrayList<Song>(amount);
        for(var e : likedAlbums) {
            var albumID = e.getKey();
            var amountForGenre = e.getValue() * amount / amountOfLikedSongs;
            var songs = likedSongsRepository.getAlbumSongsNotLikedByUser(userID, albumID);
            Collections.shuffle(songs);
            recommendation.addAll(songs.stream().limit(amountForGenre).toList());
        }
        fillLeftSongsByRandom(userID, amount, recommendation);
        return recommendation;
    }

}
