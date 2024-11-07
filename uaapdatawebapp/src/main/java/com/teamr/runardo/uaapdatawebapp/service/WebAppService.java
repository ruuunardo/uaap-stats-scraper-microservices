package com.teamr.runardo.uaapdatawebapp.service;

import com.teamr.runardo.uaapdatawebapp.model.UaapSeason;

import java.util.List;
import java.util.Optional;

public interface WebAppService {
    List<UaapSeason> fetchAllUaapSeason();

    UaapSeason saveUaapSeason(UaapSeason uaapSeason);

    void deleteUaapSeasonById(String seasonId);

    UaapSeason findUaapSeasonById(String id);

    void deleteUaapGamesByIds(Optional<List<String>> selections);

    void updateUaapSeasonGamesById(String id);
}
