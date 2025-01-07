package com.teamr.runardo.uaapstatscraper.controller;

import com.teamr.runardo.uaapstatscraper.dto.*;
import com.teamr.runardo.uaapstatscraper.dto.playerstat.BballPlayerStat;
import com.teamr.runardo.uaapstatscraper.dto.playerstat.PlayerStat;
import com.teamr.runardo.uaapstatscraper.dto.playerstat.VballPlayerStat;
import com.teamr.runardo.uaapstatscraper.service.IGameScraperService;
import io.github.resilience4j.retry.annotation.Retry;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private static final Logger log = LoggerFactory.getLogger(GameScraperController.class);
    private final IGameScraperService accountService;

    public GameScraperController(IGameScraperService accountService) {
        this.accountService = accountService;
    }


//    @Operation(
//            summary = "Scrape UAAP Games REST API",
//            description = "REST API to scrape UAAP Games from stats website"
//    )
//    @ApiResponses({
//            @ApiResponse(
//                    responseCode = "200",
//                    description = "HTTP Status OK",
//                    content = @Content(schema = @Schema(implementation = UaapGameDto.class))
//            ),
//            @ApiResponse(
//                    responseCode = "500",
//                    description = "HTTP Status Internal Server Error",
//                    content = @Content(
//                            schema = @Schema(implementation = ErrorResponseDto.class)
//                    )
//            )
//    }
//    )
//    @PostMapping("/scrape/game")
//    public ResponseEntity<List<UaapGameDto>> scrapeAllGames(@Valid @RequestBody UaapSeasonDto uaapSeasonDto) {
//        List<UaapGameDto> allGames = accountService.getAllGames(uaapSeasonDto);
//
//        return ResponseEntity
//                .status(HttpStatus.OK)
//                .body(allGames);
//    }

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
    @GetMapping("/games")
    public ResponseEntity<List<UaapGameDto>> scrapeAllGames(@RequestParam int seasonNumber, @RequestParam @Pattern(regexp = "^(http)s?.*:id.*") String url, @RequestParam String gameCode, @RequestParam String gameName) {
        UaapSeasonDto uaapSeasonDto = new UaapSeasonDto(seasonNumber, url, new UaapGameCode(gameCode, gameName));
        List<UaapGameDto> allGames = accountService.getAllGames(uaapSeasonDto);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(allGames);
    }

    @Operation(
            summary = "Scrape UAAP Player Stats REST API",
            description = "REST API to scrape UAAP Player Stats from stats website"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "HTTP Status OK",
                    content = @Content(schema = @Schema(implementation = BballPlayerStat.class))
            ),
            @ApiResponse(
                    responseCode = "200",
                    description = "HTTP Status OK",
                    content = @Content(schema = @Schema(implementation = VballPlayerStat.class))
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
    @GetMapping(value = "/stats")
    @Retry(name = "scrapeGameStat", fallbackMethod = "scrapeGameStatFallback")
    public ResponseEntity<HashMap<String, List<PlayerStat>>> scrapeGameStat(@RequestParam int seasonNumber, @RequestParam @Pattern(regexp = "^(http)s?.*:id.*") String url, @RequestParam String gameCode, @RequestParam String gameName, @RequestParam Integer gameNumber) {
        UaapSeasonDto uaapSeasonDto = new UaapSeasonDto(seasonNumber, url, new UaapGameCode(gameCode, gameName));
        HashMap<String, List<PlayerStat>> playerStats = accountService.getUaapGamePlayerStats(uaapSeasonDto, gameNumber);
        log.info("scrapegames invoked!");
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(playerStats);
    }

    public ResponseEntity<ResponseDto> scrapeGameStatFallback(@RequestParam int seasonNumber, @RequestParam @Pattern(regexp = "^(http)s?.*:id.*") String url, @RequestParam String gameCode, @RequestParam String gameName, @RequestParam Integer gameNumber, Throwable throwable) {
        log.info("scrapegames fallback invoked!");
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ResponseDto("500", "INTERNAL_SERVER_ERROR"));
    }
//    -----------------------------------------------------------

//    @Operation(
//            summary = "Scrape UAAP Player Stats REST API",
//            description = "REST API to scrape UAAP Player Stats from stats website"
//    )
//    @ApiResponses({
//            @ApiResponse(
//                    responseCode = "200",
//                    description = "HTTP Status OK",
//                    content = @Content(schema = @Schema(implementation = BballPlayerStat.class))
//            ),
//            @ApiResponse(
//                    responseCode = "200",
//                    description = "HTTP Status OK",
//                    content = @Content(schema = @Schema(implementation = VballPlayerStat.class))
//            ),
//            @ApiResponse(
//                    responseCode = "500",
//                    description = "HTTP Status Internal Server Error",
//                    content = @Content(
//                            schema = @Schema(implementation = ErrorResponseDto.class)
//                    )
//            )
//    }
//    )
//    @PostMapping(value = "/scrape/stats")
//    @Retry(name = "scrapeGameStat", fallbackMethod = "scrapeGameStatFallback")
//    public ResponseEntity<HashMap<String, List<PlayerStat>>> scrapeGameStat(@Valid @RequestBody UaapSeasonDto uaapSeasonDto, @RequestParam Integer gameNumber) {
//        HashMap<String, List<PlayerStat>> playerStats = accountService.getUaapGamePlayerStats(uaapSeasonDto, gameNumber);
//        log.info("scrapegames invoked!");
//        return ResponseEntity
//                .status(HttpStatus.OK)
//                .body(playerStats);
//    }
//
//    public ResponseEntity<ResponseDto> scrapeGameStatFallback(@Valid @RequestBody UaapSeasonDto uaapSeasonDto, @RequestParam Integer gameNumber, Throwable throwable) {
//        log.info("scrapegames fallback invoked!");
//        return ResponseEntity
//                .status(HttpStatus.INTERNAL_SERVER_ERROR)
//                .body(new ResponseDto("500", "INTERNAL_SERVER_ERROR"));
//    }

//    @PostMapping("/update/game")
//    public ResponseEntity<UaapGameDto> scrapeGame(@Valid @RequestBody UaapSeasonDto uaapSeasonDto, @RequestParam Integer gameNumber) {
//        UaapGameDto game = accountService.updateGame(uaapSeasonDto, gameNumber);
//
//        return ResponseEntity
//                .status(HttpStatus.OK)
//                .body(game);
//    }
}


