package com.teamr.runardo.uaapstatsdata.service;

import com.teamr.runardo.uaapstatsdata.dto.UaapGameDto;
import com.teamr.runardo.uaapstatsdata.dto.UaapSeasonDto;
import com.teamr.runardo.uaapstatsdata.entity.PlayerStat;
import com.teamr.runardo.uaapstatsdata.entity.UaapGame;
import com.teamr.runardo.uaapstatsdata.entity.UaapSeason;

import java.util.List;

public interface UaapDataService {
    List<UaapSeason> findAllUaapSeason();

    UaapSeason findUaapSeasonById(String seasonId);

    UaapSeason saveUaapSeason(UaapSeasonDto uaapSeason);

    Boolean deleteUaapSeasonById(String seasonId);

    Boolean updateUaapSeason(UaapSeasonDto uaapSeasonDto);


    UaapGame saveUaapGame(UaapGameDto uaapGame);

    Boolean updateUaapGame(UaapGameDto uaapGameDto);

    UaapGame findUaapGameById(String gameId);

    Boolean deleteUaapGameById(String gameId);

    List<? extends PlayerStat> findUaapStats(String code);

    <T extends PlayerStat> PlayerStat saveUaapStats(T stat);

    <T extends PlayerStat> void saveUaapStats(List<T> stat);

    void deleteUaapStatsByGameId(String gameId);

    void deleteAllPlayersBySeasonId(String seasonId);
}
