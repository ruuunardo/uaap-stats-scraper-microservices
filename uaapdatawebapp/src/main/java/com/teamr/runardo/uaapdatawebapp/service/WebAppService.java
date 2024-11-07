package com.teamr.runardo.uaapdatawebapp.service;

import com.teamr.runardo.uaapdatawebapp.model.PlayerStat;
import com.teamr.runardo.uaapdatawebapp.model.UaapSeason;
import com.teamr.runardo.uaapdatawebapp.model.dto.UaapSeasonDto;

import java.util.List;
import java.util.Optional;

public interface WebAppService {
    List<UaapSeason> fetchAllUaapSeason();

    void saveUaapSeason(UaapSeason uaapSeason);

    void deleteUaapSeasonById(String seasonId);

    UaapSeason findUaapSeasonById(String id);

    void deleteUaapGamesByIds(Optional<List<String>> selections);

    void updateUaapSeasonGamesById(String id);

    void saveUaapSeasonList(List<UaapSeasonDto> uaapSeasonList);

    List<? extends PlayerStat> fetchUaapStatsByGameIds(Optional<List<String>> selections, String id);
}
