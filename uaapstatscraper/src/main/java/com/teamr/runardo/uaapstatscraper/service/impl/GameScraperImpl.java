package com.teamr.runardo.uaapstatscraper.service.impl;

import com.teamr.runardo.uaapstatscraper.dto.UaapGameDto;
import com.teamr.runardo.uaapstatscraper.dto.UaapSeasonDto;
import com.teamr.runardo.uaapstatscraper.dto.playerstat.PlayerStat;
import com.teamr.runardo.uaapstatscraper.scraper.GameScraper;
import com.teamr.runardo.uaapstatscraper.service.IGameScraperService;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Stream;

@Service
public class GameScraperImpl implements IGameScraperService {

    @Override
    public List<UaapGameDto> getAllGames(UaapSeasonDto uaapSeasonDto) {
        GameScraper gameScraper = GameScraper.gameScraperFactory(uaapSeasonDto);
        List<UaapGameDto> uaapGameDtoDtos = gameScraper.scrapeAllGamesAndResults().orElseThrow(
                () -> new RuntimeException(String.format("No game data found: %s", uaapSeasonDto.getUrl()))
        );
        return uaapGameDtoDtos;
    }

    @Override
    public HashMap<String, List<PlayerStat>> getUaapGamePlayerStats(UaapSeasonDto uaapSeasonDto, Integer gameNumber) {
        GameScraper gameScraper = GameScraper.gameScraperFactory(uaapSeasonDto);
        HashMap<String, List<PlayerStat>> stringListHashMap = gameScraper.scrapeAllPlayerStatsfromGame(gameNumber);
        return stringListHashMap;
    }


    @Override
    public UaapGameDto updateGame(UaapSeasonDto uaapSeasonDto, Integer gameNumber) {
        GameScraper gameScraper = GameScraper.gameScraperFactory(uaapSeasonDto);
        UaapGameDto uaapGameDto = gameScraper.updateAdditionalData(gameNumber);
        return uaapGameDto;
    }

}
