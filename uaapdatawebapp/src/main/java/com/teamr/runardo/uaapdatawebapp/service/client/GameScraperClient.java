package com.teamr.runardo.uaapdatawebapp.service.client;

import com.teamr.runardo.uaapdatawebapp.model.BballPlayerStat;
import com.teamr.runardo.uaapdatawebapp.model.PlayerStat;
import com.teamr.runardo.uaapdatawebapp.model.VballPlayerStat;
import com.teamr.runardo.uaapdatawebapp.model.dto.UaapGameDto;
import com.teamr.runardo.uaapdatawebapp.model.dto.UaapSeasonDto;
import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashMap;
import java.util.List;

@FeignClient("uaapstatscraper")
public interface GameScraperClient {
    @PostMapping(value = "api/scrape/game", consumes = "application/json")
    public ResponseEntity<List<UaapGameDto>> scrapeAllGames(@RequestBody UaapSeasonDto uaapSeasonDto);

    @PostMapping(value = "api/scrape/stats", consumes = "application/json")
    public ResponseEntity<HashMap<String, List<? extends PlayerStat>>> scrapeGameStat(@RequestBody UaapSeasonDto uaapSeasonDto, @RequestParam Integer gameNumber);
}
