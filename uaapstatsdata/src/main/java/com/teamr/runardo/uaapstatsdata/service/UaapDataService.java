package com.teamr.runardo.uaapstatsdata.service;

import com.teamr.runardo.uaapstatsdata.entity.UaapSeason;
import jakarta.validation.constraints.Min;

import java.util.List;
import java.util.Optional;

public interface UaapDataService {
    UaapSeason saveUaapSeason(UaapSeason uaapSeason);

    Optional<UaapSeason> findUaapSeasonBySeasonAndGameCode(int seasonNum, String gameCode);

    Boolean deleteBySeasonAndGameCode(@Min(value = 1, message = "Season number should be a whole number (>0)") int seasonNumber, String gameCode);

    List<UaapSeason> findAllUaapSeason();
}
