package com.teamr.runardo.uaapstatscraper.service;

import com.teamr.runardo.uaapstatscraper.dto.UaapGame;
import com.teamr.runardo.uaapstatscraper.dto.UaapSeason;
import com.teamr.runardo.uaapstatscraper.dto.playerstat.PlayerStat;

import java.util.HashMap;
import java.util.List;

public interface IGameScraperService {
    List<UaapGame> getAllGames(UaapSeason uaapSeason);

    HashMap<String, List<PlayerStat>> getUaapGamePlayerStats(UaapSeason uaapSeason, Integer gameNumber);

    UaapGame updateGame(UaapSeason uaapSeason, Integer gameNumber);
}
