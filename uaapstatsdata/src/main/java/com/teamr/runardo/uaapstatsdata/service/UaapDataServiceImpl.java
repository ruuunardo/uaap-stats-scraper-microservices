package com.teamr.runardo.uaapstatsdata.service;

import com.teamr.runardo.uaapstatsdata.entity.UaapSeason;
import com.teamr.runardo.uaapstatsdata.exception.ResourceNotFoundException;
import com.teamr.runardo.uaapstatsdata.repository.UaapSeasonRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@AllArgsConstructor
public class UaapDataServiceImpl implements UaapDataService{
    private UaapSeasonRepository uaapSeasonRepository;

    @Override
    public UaapSeason saveUaapSeason(UaapSeason uaapSeason) {
        return uaapSeasonRepository.customSaveGame(uaapSeason);
    }

    @Override
    public Optional<UaapSeason> findUaapSeasonBySeasonAndGameCode(int seasonNum, String gameCode) {
        return uaapSeasonRepository.findAllBySeasonNumberAndGameCode(seasonNum, gameCode);
    }

    @Override
    public Boolean deleteBySeasonAndGameCode(int seasonNumber, String gameCode) {
        boolean isDelete = false;
        UaapSeason uaapSeason = uaapSeasonRepository.findAllBySeasonNumberAndGameCode(seasonNumber, gameCode).orElseThrow(
                () -> new ResourceNotFoundException("UaapSeason", "season-gameCode", seasonNumber + "-" + gameCode)
        );

        uaapSeasonRepository.delete(uaapSeason);
        isDelete = true;

        return isDelete;
    }

    @Override
    public List<UaapSeason> findAllUaapSeason() {
        List<UaapSeason> uaapSeasons = uaapSeasonRepository.findAll();


        return uaapSeasons;
    }


}
