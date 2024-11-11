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

import java.util.Collections;
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
    public void saveUaapSeason(UaapSeason uaapSeason) {
        UaapSeasonDto uaapSeasonDto = UaapSeasonMapper.mapToUaapSeasonDto(uaapSeason, new UaapSeasonDto());
        if (uaapSeason.getId() == null || uaapSeason.getId().isEmpty()) {
            uaapDataClient.createUaapSeason(uaapSeasonDto);
        } else {
            uaapDataClient.updateUaapSeason(uaapSeasonDto);
        }
    }

    @Override
    public void deleteUaapSeasonById(String seasonId) {
        uaapDataClient.deleteUaapSeasonById(seasonId);
    }

    @Override
    public UaapSeason findUaapSeasonById(String id) {
        ResponseEntity<UaapSeason> uaapSeasonResponseEntity = uaapDataClient.fetchUaapSeasonById(id);
        UaapSeason uaapSeason = uaapSeasonResponseEntity.getBody();
        Collections.sort(uaapSeason.getUaapGames(), Collections.reverseOrder());
        return uaapSeason;
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
        HashMap<String, List<? extends PlayerStat>> hashMap = gameScraperClient.scrapeGameStat(uaapSeasonDto, g.getGameNumber()).getBody();
        List<? extends PlayerStat> playerStats = hashMap.values().stream().flatMap(List::stream).toList();
        uaapDataClient.createUaapStats(playerStats);
    }

    @Override
    public void saveUaapSeasonList(List<UaapSeasonDto> uaapSeasonList) {
        for (UaapSeasonDto seasonDto : uaapSeasonList) {
            uaapDataClient.createUaapSeason(seasonDto);
        }
    }

    @Override
    public List<? extends PlayerStat> fetchUaapStatsByGameIds(Optional<List<String>> selections, String id) {
        List<String> strings = selections.get();
        String gameCode = id.split("-")[1];
            List<? extends PlayerStat> stats = strings.stream().map(
                    s -> {
                        return uaapDataClient.fetchUaapStats(s).getBody();
                    }
            ).flatMap(List::stream).toList();
            return stats;
    }
}
