package com.phoenixplaydev.musicapp.service;

import com.phoenixplaydev.musicapp.model.Tables;
import com.phoenixplaydev.musicapp.model.tables.pojos.Singer;
import com.phoenixplaydev.musicapp.repository.SingerRepository;
import org.jooq.exception.DataAccessException;
import org.jooq.exception.NoDataFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SingerService {

    @Autowired
    private SingerRepository singerRepository;

    public Singer getSingerByID(Long singerID) {
        if(singerRepository.isSingerNotExists(singerID))
            throw new NoDataFoundException("Singer with ID = " + singerID + " not found!");
        return singerRepository.getSingerByID(singerID);
    }

    public List<Singer> getAllSingers() {
        return singerRepository.getAllSingers();
    }

    public void addSinger(String name, byte[] cover) {
        Singer singer = new Singer();
        if (name == null || name.isBlank())
            throw new IllegalArgumentException("Singer name can't be null or empty!");
        singer.setName(name);
        singer.setCover(cover);
    }

    public void updateSingerInfo(Long id, String name, byte[] cover) {
        if(singerRepository.isSingerNotExists(id))
            throw new DataAccessException("Can't update non-existing singer with ID = " + id + "!");
        Singer singer = singerRepository.getSingerByID(id);

        if(name != null) {
            if (name.isBlank())
                throw new IllegalArgumentException("Singer name can't be empty!");
            singer.setName(name);
        }
        if(cover != null)
            singer.setCover(cover);

        singerRepository.updateSingerInfo(singer);
    }

    public void deleteSinger(Long singerID) {
        if(singerRepository.isSingerNotExists(singerID))
            throw new DataAccessException("Can't delete non-existing singer with ID = " + singerID + "!");
        singerRepository.deleteSinger(singerID);
    }

}
