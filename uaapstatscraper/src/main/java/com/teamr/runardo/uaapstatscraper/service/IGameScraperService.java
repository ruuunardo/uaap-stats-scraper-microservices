package com.teamr.runardo.uaapstatscraper.service;

import com.teamr.runardo.uaapstatscraper.dto.UaapGameDto;
import com.teamr.runardo.uaapstatscraper.dto.UaapSeasonDto;
import com.teamr.runardo.uaapstatscraper.dto.playerstat.PlayerStat;

import java.util.HashMap;
import java.util.List;

public interface IGameScraperService {
    List<UaapGameDto> getAllGames(UaapSeasonDto uaapSeasonDto);

    HashMap<String, List<PlayerStat>> getUaapGamePlayerStats(UaapSeasonDto uaapSeasonDto, Integer gameNumber);

    UaapGameDto updateGame(UaapSeasonDto uaapSeasonDto, Integer gameNumber);
}
