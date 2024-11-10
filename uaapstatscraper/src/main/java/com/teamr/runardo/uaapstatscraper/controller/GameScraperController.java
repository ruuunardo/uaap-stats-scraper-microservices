package com.teamr.runardo.uaapstatscraper.controller;

import com.teamr.runardo.uaapstatscraper.dto.ErrorResponseDto;
import com.teamr.runardo.uaapstatscraper.dto.UaapGameDto;
import com.teamr.runardo.uaapstatscraper.dto.UaapSeasonDto;
import com.teamr.runardo.uaapstatscraper.dto.playerstat.PlayerStat;
import com.teamr.runardo.uaapstatscraper.service.IGameScraperService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
        name = "REST APIs for UAAP Game Scraper",
        description = "REST APIs in UAAP Game Scraper to FETCH games and player stats"
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

    @Operation(
            summary = "Scrape UAAP Games REST API",
            description = "REST API to scrape UAAP Games from stats website"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "HTTP Status OK",
                    content = @Content(schema = @Schema(implementation = UaapGameDto.class))
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "HTTP Status Internal Server Error",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponseDto.class)
                    )
            )
    }
    )
    @PostMapping("/scrape/game")
    public ResponseEntity<List<UaapGameDto>> scrapeAllGames(@Valid @RequestBody UaapSeasonDto uaapSeasonDto) {
        List<UaapGameDto> allGames = accountService.getAllGames(uaapSeasonDto);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(allGames);
    }


//    @PostMapping(value = "/scrape/stats/BB")
//    public ResponseEntity<HashMap<String, List<PlayerStat>>> scrapeGameStatBball(@Valid @RequestBody UaapSeasonDto uaapSeasonDto, @RequestParam Integer gameNumber) {
//        HashMap<String, List<PlayerStat>> playerStats = accountService.getUaapGamePlayerStats(uaapSeasonDto, gameNumber);
//
//        return ResponseEntity
//                .status(HttpStatus.OK)
//                .body(playerStats);
//    }
//
//    @PostMapping(value = "/scrape/stats/VB")
//    public ResponseEntity<HashMap<String, List<PlayerStat>>> scrapeGameStatVball(@Valid @RequestBody UaapSeasonDto uaapSeasonDto, @RequestParam Integer gameNumber) {
//        HashMap<String, List<PlayerStat>> playerStats = accountService.getUaapGamePlayerStats(uaapSeasonDto, gameNumber);
//
//        return ResponseEntity
//                .status(HttpStatus.OK)
//                .body(playerStats);
//    }

    @PostMapping(value = "/scrape/stats")
    public ResponseEntity<HashMap<String, List<PlayerStat>>> scrapeGameStat(@Valid @RequestBody UaapSeasonDto uaapSeasonDto, @RequestParam Integer gameNumber) {
        HashMap<String, List<PlayerStat>> playerStats = accountService.getUaapGamePlayerStats(uaapSeasonDto, gameNumber);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(playerStats);
    }

    @PostMapping("/scrape/statstest")
    public ResponseEntity<List<PlayerStat>> scrapeGameStatTest(@Valid @RequestBody UaapSeasonDto uaapSeasonDto, @RequestParam Integer gameNumber) {
        List<PlayerStat> playerStats = accountService.getUaapGamePlayerStatsTest(uaapSeasonDto, gameNumber);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(playerStats);
    }

    @PostMapping("/update/game")
    public ResponseEntity<UaapGameDto> scrapeGame(@Valid @RequestBody UaapSeasonDto uaapSeasonDto, @RequestParam Integer gameNumber) {
        UaapGameDto game = accountService.updateGame(uaapSeasonDto, gameNumber);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(game);
    }
}


