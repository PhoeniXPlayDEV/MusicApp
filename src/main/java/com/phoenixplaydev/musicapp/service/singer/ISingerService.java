package com.phoenixplaydev.musicapp.service.singer;

import com.phoenixplaydev.musicapp.model.tables.pojos.Singer;

import java.util.List;

public interface ISingerService {

    Long getSingerByID(java.lang.Long id);

    List<Long> getAllSingers();

    void addSinger(Singer singer);

    void updateSingerInfo(Singer singer);

}
