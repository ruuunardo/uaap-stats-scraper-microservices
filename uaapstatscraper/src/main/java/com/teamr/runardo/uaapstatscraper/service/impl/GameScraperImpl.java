package com.teamr.runardo.uaapstatscraper.service.impl;

import com.teamr.runardo.uaapstatscraper.dto.UaapGame;
import com.teamr.runardo.uaapstatscraper.dto.UaapSeason;
import com.teamr.runardo.uaapstatscraper.dto.playerstat.PlayerStat;
import com.teamr.runardo.uaapstatscraper.scraper.GameScraper;
import com.teamr.runardo.uaapstatscraper.service.IGameScraperService;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

@Service
public class GameScraperImpl implements IGameScraperService {

    @Override
    public List<UaapGame> getAllGames(UaapSeason uaapSeason) {
        GameScraper gameScraper = GameScraper.gameScraperFactory(uaapSeason);
        List<UaapGame> uaapGameDtos = gameScraper.scrapeAllGamesAndResults().orElseThrow(
                () -> new RuntimeException(String.format("No game data found: %s", uaapSeason.getUrl()))
        );
        return uaapGameDtos;
    }

    @Override
    public HashMap<String, List<PlayerStat>> getUaapGamePlayerStats(UaapSeason uaapSeason, Integer gameNumber) {
        GameScraper gameScraper = GameScraper.gameScraperFactory(uaapSeason);
        HashMap<String, List<PlayerStat>> stringListHashMap = gameScraper.scrapeAllPlayerStatsfromGame(gameNumber);
        return stringListHashMap;
    }

    @Override
    public UaapGame updateGame(UaapSeason uaapSeason, Integer gameNumber) {
        GameScraper gameScraper = GameScraper.gameScraperFactory(uaapSeason);
        UaapGame uaapGame = gameScraper.updateAdditionalData(gameNumber);
        return uaapGame;
    }
}
