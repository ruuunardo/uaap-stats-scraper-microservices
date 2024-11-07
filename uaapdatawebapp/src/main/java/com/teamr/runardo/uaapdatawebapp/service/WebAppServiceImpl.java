package com.teamr.runardo.uaapdatawebapp.service;

import com.teamr.runardo.uaapdatawebapp.mapper.UaapGameMapper;
import com.teamr.runardo.uaapdatawebapp.mapper.UaapSeasonMapper;
import com.teamr.runardo.uaapdatawebapp.model.*;
import com.teamr.runardo.uaapdatawebapp.model.dto.UaapGameDto;
import com.teamr.runardo.uaapdatawebapp.model.dto.UaapSeasonDto;
import com.teamr.runardo.uaapdatawebapp.service.client.GameScraperClient;
import com.teamr.runardo.uaapdatawebapp.service.client.UaapDataClient;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class WebAppServiceImpl implements WebAppService{
    private UaapDataClient uaapDataClient;
    private GameScraperClient gameScraperClient;

    @Override
    public List<UaapSeason> fetchAllUaapSeason() {
        ResponseEntity<List<UaapSeason>> listResponseEntity = uaapDataClient.fetchAllUaapSeason();
        return listResponseEntity.getBody();
    }

    @Override
    public UaapSeason saveUaapSeason(UaapSeason uaapSeason) {
        UaapSeasonDto uaapSeasonDto = UaapSeasonMapper.mapToUaapSeasonDto(uaapSeason, new UaapSeasonDto());
        uaapDataClient.createUaapSeason(uaapSeasonDto);
        UaapSeasonMapper.mapToUaapSeason(uaapSeasonDto, uaapSeason);
        return uaapSeason;
    }

    @Override
    public void deleteUaapSeasonById(String seasonId) {
        uaapDataClient.deleteUaapSeasonById(seasonId);
    }

    @Override
    public UaapSeason findUaapSeasonById(String id) {
        ResponseEntity<UaapSeason> uaapSeasonResponseEntity = uaapDataClient.fetchUaapSeasonById(id);
        return uaapSeasonResponseEntity.getBody();
    }

    @Override
    public void deleteUaapGamesByIds(Optional<List<String>> selections) {
        selections.ifPresent(
                list -> list.forEach(id -> uaapDataClient.deleteUaapGameById(id))
        );
    }

    @Override
    public void updateUaapSeasonGamesById(String id) {
        UaapSeason uaapSeasonDb = findUaapSeasonById(id);
        List<UaapGame> uaapGamesDb = uaapSeasonDb.getUaapGames();
        List<UaapGameDto> uaapGameDtosDb = uaapGamesDb.stream().map(
                g -> {
                    UaapGameDto uaapGameDto = UaapGameMapper.mapToUaapGameDto(g, new UaapGameDto());
                    uaapGameDto.setUrl(uaapSeasonDb.getUrl());
                    return uaapGameDto;
                }
        ).toList();

        UaapSeasonDto uaapSeasonDto = UaapSeasonMapper.mapToUaapSeasonDto(uaapSeasonDb, new UaapSeasonDto());
        List<UaapGameDto> uaapGameDtosWeb = gameScraperClient.scrapeAllGames(uaapSeasonDto).getBody();

        List<UaapGameDto> uaapGamesMissing = uaapGameDtosWeb.stream().filter(
                g -> !uaapGameDtosDb.contains(g)
        ).toList();

        for (UaapGameDto gameDto : uaapGamesMissing) {
            uaapDataClient.updateUaapGame(gameDto);
            extractAndSavePlayerStats(gameDto, uaapSeasonDto);
            break;
        }
    }

    private void extractAndSavePlayerStats(UaapGameDto g, UaapSeasonDto uaapSeasonDto) {
        String gameCode = uaapSeasonDto.getGameCode().getGameCode();
        if (gameCode.endsWith("BB")) {
            HashMap<String, List<BballPlayerStat>> hashMap = gameScraperClient.scrapeGameStatBball(uaapSeasonDto, g.getGameNumber()).getBody();
            List<BballPlayerStat> playerStats = hashMap.values().stream().flatMap(List::stream).toList();
            uaapDataClient.createUaapStatsBball(playerStats);
        } else if (gameCode.endsWith("VB")) {
            HashMap<String, List<VballPlayerStat>> hashMap = gameScraperClient.scrapeGameStatVball(uaapSeasonDto, g.getGameNumber()).getBody();
            List<VballPlayerStat> playerStats = hashMap.values().stream().flatMap(List::stream).toList();
            uaapDataClient.createUaapStatsVball(playerStats);
        } else {
            throw new RuntimeException("Game Code not supported: " + gameCode);
        }
    }
}
