package com.teamr.runardo.uaapstatscraper.controller;

import com.teamr.runardo.uaapstatscraper.dto.ResponseDto;
import com.teamr.runardo.uaapstatscraper.dto.UaapGame;
import com.teamr.runardo.uaapstatscraper.dto.UaapSeason;
import com.teamr.runardo.uaapstatscraper.dto.playerstat.PlayerStat;
import com.teamr.runardo.uaapstatscraper.service.IGameScraperService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@Tag(
        name = "CRUD REST APIs for UAAP Game Scraper",
        description = "CRUD REST APIs in UAAP Game Scrape to FETCH games"
)
@RestController
@RequestMapping(value = "/api", produces = MediaType.APPLICATION_JSON_VALUE)
@Validated
public class GameScraperController {
    private final IGameScraperService accountService;

    public GameScraperController(IGameScraperService accountService) {
        this.accountService = accountService;
    }

    @GetMapping("/hello")
    public String sayHello() {
        return "Hello World!";
    }

    @GetMapping("/scrape/game")
    public ResponseEntity<List<UaapGame>> scrapeAllGames(@Valid @RequestBody UaapSeason uaapSeason) {
        List<UaapGame> allGames = accountService.getAllGames(uaapSeason);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(allGames);
    }

    @GetMapping("/scrape/stats")
    public ResponseEntity<HashMap<String, List<PlayerStat>>> scrapeGameStat(@Valid @RequestBody UaapSeason uaapSeason, @RequestParam Integer gameNumber) {
        HashMap<String, List<PlayerStat>> playerStats = accountService.getUaapGamePlayerStats(uaapSeason, gameNumber);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(playerStats);
    }

    @GetMapping("/update/game")
    public ResponseEntity<UaapGame> scrapeGame(@Valid @RequestBody UaapSeason uaapSeason, @RequestParam Integer gameNumber) {
        UaapGame game = accountService.updateGame(uaapSeason, gameNumber);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(game);
    }
}


