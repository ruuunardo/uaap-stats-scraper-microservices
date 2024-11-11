package com.teamr.runardo.uaapstatsdata.controller;

import com.teamr.runardo.uaapstatsdata.constants.UaapDataConstants;
import com.teamr.runardo.uaapstatsdata.dto.*;
import com.teamr.runardo.uaapstatsdata.entity.*;
import com.teamr.runardo.uaapstatsdata.exception.UaapSeasonAlreadyExistsException;
import com.teamr.runardo.uaapstatsdata.mapper.UaapGameMapper;
import com.teamr.runardo.uaapstatsdata.mapper.UaapSeasonMapper;
import com.teamr.runardo.uaapstatsdata.service.UaapDataService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Tag(
        name = "CRUD REST APIs for UAAP Stats",
        description = "CRUD REST APIs in UAAP Stats to CREATE, UPDATE, FETCH AND DELETE UAAP data"
)
@RestController
@RequestMapping(value = "/api", produces = MediaType.APPLICATION_JSON_VALUE)
@Validated
public class UaapDataController {
    private static final Logger log = LoggerFactory.getLogger(UaapDataController.class);

    private final UaapDataService uaapDataService;

    @Autowired
    public UaapDataController(UaapDataService uaapDataService) {
        this.uaapDataService = uaapDataService;
    }


    @PostMapping("/create/uaapseason")
    public ResponseEntity<ResponseDto> createUaapSeason(@Valid @RequestBody UaapSeasonDto uaapSeasonDto) {
        uaapDataService.saveUaapSeason(uaapSeasonDto);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new ResponseDto(UaapDataConstants.STATUS_201, UaapDataConstants.MESSAGE_201));
    }

    /**
     * FETCH ALL UAAP SEASON
     *
     *  @return {@code ResponseEntity} with body-- {@code List} of {@link UaapSeason}
     */
    @Operation(
            summary = "Fetch All UAAP Season REST API",
            description = "REST API to fetch All UAAP Season from database"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "HTTP Status OK",
                    content = @Content(schema = @Schema(implementation = UaapSeason.class))
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
    @GetMapping("/fetch/uaapseason")
    public ResponseEntity<List<UaapSeason>> fetchAllUaapSeason() {
        List<UaapSeason> uaapSeasons = uaapDataService.findAllUaapSeason();

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(uaapSeasons);
    }

    /**
     * FETCH UAAP SEASON BY ID
     * @param seasonId
     *  @return {@code ResponseEntity} with body-- {@link UaapSeason}
     */
    @Operation(
            summary = "Fetch UAAP Season REST API by Id",
            description = "REST API to fetch UAAP Season from database"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "HTTP Status OK",
                    content = @Content(schema = @Schema(implementation = UaapSeason.class))
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
    @GetMapping("/fetch/uaapseason/{seasonId}")
    public ResponseEntity<UaapSeason> fetchUaapSeasonById(@PathVariable String seasonId) {
        UaapSeason uaapSeason = uaapDataService.findUaapSeasonById(seasonId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(uaapSeason);
    }


    /**
     * DELETE UAAP SEASON BY ID
     * @param seasonId
     *  @return {@code ResponseEntity} with body-- {@link ResponseDto}
     */
    @Operation(
            summary = "Delete UAAP Season REST API by Id",
            description = "REST API to delete UAAP Season from database"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "HTTP Status OK",
                    content = @Content(schema = @Schema(implementation = ResponseDto.class))
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
    @DeleteMapping("/delete/uaapseason")
    public ResponseEntity<ResponseDto> deleteUaapSeasonById(@RequestParam String seasonId) {
        Boolean isDeleted = uaapDataService.deleteUaapSeasonById(seasonId);

        if (isDeleted) {
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new ResponseDto(UaapDataConstants.STATUS_200, UaapDataConstants.MESSAGE_200));
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseDto(UaapDataConstants.STATUS_417, UaapDataConstants.MESSAGE_417_DELETE));
        }
    }

    /**
     * UPDATE UAAP SEASON
     * @param uaapSeasonDto
     *  @return {@code ResponseEntity} with body-- {@link ResponseDto}
     */
    @Operation(
            summary = "Update UAAP Season REST API",
            description = "REST API to update UAAP Season from database"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "HTTP Status OK",
                    content = @Content(schema = @Schema(implementation = ResponseDto.class))
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
    @PutMapping("/update/uaapseason")
    public ResponseEntity<ResponseDto> updateUaapSeason(@Valid @RequestBody UaapSeasonDto uaapSeasonDto) {
        uaapDataService.updateUaapSeason(uaapSeasonDto);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ResponseDto(UaapDataConstants.STATUS_200, UaapDataConstants.MESSAGE_200));
    }



    /**
     * FETCH UAAP GAME BY ID
     * @param gameId
     *  @return {@code ResponseEntity} with body-- {@link UaapGame}
     */
    @Operation(
            summary = "Fetch UAAP Game REST API by Id",
            description = "REST API to fetch UAAP Game from database"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "HTTP Status OK",
                    content = @Content(schema = @Schema(implementation = UaapGame.class))
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
    @GetMapping("/fetch/uaapgame/{gameId}")
    public ResponseEntity<UaapGame> fetchUaapGame(@PathVariable String gameId) {
        UaapGame game = uaapDataService.findUaapGameById(gameId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(game);
    }

    /**
     * CREATE UAAP GAME
     * @param uaapGameDto
     *  @return {@code ResponseEntity} with body-- {@link UaapGame}
     */
    @Operation(
            summary = "Create UAAP Game REST API by Id",
            description = "REST API to create UAAP Game from database"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "HTTP Status OK",
                    content = @Content(schema = @Schema(implementation = UaapGame.class))
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
    @PostMapping("/create/uaapgame")
    public ResponseEntity<UaapGame> createUaapGame(@RequestBody UaapGameDto uaapGameDto) {
        UaapGame game = uaapDataService.saveUaapGame(uaapGameDto);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(game);
    }

    /**
     * UPDATE UAAP GAME
     * @param uaapGameDto
     *  @return {@code ResponseEntity} with body-- {@link ResponseDto}
     */
    @Operation(
            summary = "Update UAAP Game REST API",
            description = "REST API to update UAAP Game from database"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "HTTP Status OK",
                    content = @Content(schema = @Schema(implementation = ResponseDto.class))
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
    @PutMapping("/update/uaapgame")
    public ResponseEntity<ResponseDto> updateUaapGame(@RequestBody UaapGameDto uaapGameDto) {
        uaapDataService.updateUaapGame(uaapGameDto);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ResponseDto(UaapDataConstants.STATUS_201, UaapDataConstants.MESSAGE_201));
    }


    /**
     * DELETE UAAP GAME BY ID
     * @param gameId
     *  @return {@code ResponseEntity} with body-- {@link ResponseDto}
     */
    @Operation(
            summary = "Delete UAAP Game REST API by Id",
            description = "REST API to delete UAAP Game from database"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "HTTP Status OK",
                    content = @Content(schema = @Schema(implementation = ResponseDto.class))
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
    @DeleteMapping("/delete/uaapgame")
    public ResponseEntity<ResponseDto> deleteUaapGameById(@RequestParam String gameId) {
        Boolean isDeleted = uaapDataService.deleteUaapGameById(gameId);

        if (isDeleted) {
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new ResponseDto(UaapDataConstants.STATUS_200, UaapDataConstants.MESSAGE_200));
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseDto(UaapDataConstants.STATUS_417, UaapDataConstants.MESSAGE_417_DELETE));
        }
    }

    /**
     * FETCH UAAP PLAYER STATS BY GAME ID
     * @param gameId
     *  @return {@code ResponseEntity} with body-- {@code List} of {@link PlayerStat}
     */
    @Operation(
            summary = "Fetch UAAP Stats REST API by Game Id",
            description = "REST API to fetch UAAP Player Stats from database"
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
    @GetMapping(value = "/fetch/uaapstats")
    public ResponseEntity<List<? extends PlayerStat>> fetchUaapStats(@RequestParam String gameId) {
        List<? extends PlayerStat> uaapStats = uaapDataService.findUaapStatsByGameId(gameId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(uaapStats);
    }

//    @PostMapping(value = "/create/uaapstats", params = "code=BB")
//    public ResponseEntity<ResponseDto> createUaapStats(@RequestBody List<BballPlayerStat> stat) {
//        log.info(stat.toString());
//        uaapDataService.saveUaapStats(stat);
//
//        return ResponseEntity
//                .status(HttpStatus.CREATED)
//                .body(new ResponseDto(UaapDataConstants.STATUS_201, UaapDataConstants.MESSAGE_201));
//    }

    @PostMapping(value = "/create/uaapstats", params = "code=BB")
    public ResponseEntity<ResponseDto> createUaapStatsBball(@RequestBody List<BballPlayerStat> stat) {
        log.info(stat.toString());
        uaapDataService.saveUaapStats(stat);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new ResponseDto(UaapDataConstants.STATUS_201, UaapDataConstants.MESSAGE_201));
    }

    @PostMapping(value = "/create/uaapstats", params = "code=VB")
    public ResponseEntity<ResponseDto> createUaapStatsVball(@RequestBody List<VballPlayerStat> stat) {
        log.info(stat.toString());
        uaapDataService.saveUaapStats(stat);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new ResponseDto(UaapDataConstants.STATUS_201, UaapDataConstants.MESSAGE_201));
    }

    @DeleteMapping(value = "/delete/uaapstats")
    public ResponseEntity<ResponseDto> deleteUaapStats(@RequestParam String gameId) {
        uaapDataService.deleteUaapStatsByGameId(gameId);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new ResponseDto(UaapDataConstants.STATUS_201, UaapDataConstants.MESSAGE_201));
    }


    @DeleteMapping(value = "/delete/players")
    public ResponseEntity<ResponseDto> deletePlayersBySeasonId(@RequestParam String seasonId) {
        uaapDataService.deleteAllPlayersBySeasonId(seasonId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ResponseDto(UaapDataConstants.STATUS_201, UaapDataConstants.MESSAGE_201));
    }
}
