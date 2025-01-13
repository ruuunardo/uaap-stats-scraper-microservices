package com.teamr.runardo.uaapdatawebapp.service.client;

import com.teamr.runardo.uaapdatawebapp.model.PlayerStat;
import com.teamr.runardo.uaapdatawebapp.model.dto.UaapGameDto;
import com.teamr.runardo.uaapdatawebapp.model.dto.UaapSeasonDto;
import jakarta.validation.constraints.Pattern;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashMap;
import java.util.List;

@FeignClient(name = "uaapstatscraper", url = "http://uaapstatscraper:8081")
public interface GameScraperClient {
    @GetMapping(value = "api/games", consumes = "application/json")
    public ResponseEntity<List<UaapGameDto>> scrapeAllGames(@RequestParam int seasonNumber, @RequestParam @Pattern(regexp = "^(http)s?.*:id.*") String url, @RequestParam String gameCode, @RequestParam String gameName);

    @GetMapping(value = "api/stats", consumes = "application/json")
    public ResponseEntity<HashMap<String, List<? extends PlayerStat>>> scrapeGameStat(@RequestParam int seasonNumber, @RequestParam @Pattern(regexp = "^(http)s?.*:id.*") String url, @RequestParam String gameCode, @RequestParam String gameName, @RequestParam Integer gameNumber);
}
