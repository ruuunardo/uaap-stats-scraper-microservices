package com.teamr.runardo.uaapstatsdata.controller;

import com.teamr.runardo.uaapstatsdata.constants.UaapDataConstants;
import com.teamr.runardo.uaapstatsdata.dto.PlayerStatDto;
import com.teamr.runardo.uaapstatsdata.dto.ResponseDto;
import com.teamr.runardo.uaapstatsdata.dto.UaapGameDto;
import com.teamr.runardo.uaapstatsdata.dto.UaapSeasonDto;
import com.teamr.runardo.uaapstatsdata.entity.*;
import com.teamr.runardo.uaapstatsdata.exception.UaapSeasonAlreadyExistsException;
import com.teamr.runardo.uaapstatsdata.mapper.UaapGameMapper;
import com.teamr.runardo.uaapstatsdata.mapper.UaapSeasonMapper;
import com.teamr.runardo.uaapstatsdata.service.UaapDataService;
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

    @GetMapping("/hello")
    public String sayHello() {
        return "Hello World!";
    }


    @PostMapping("/create/uaapseason")
    public ResponseEntity<ResponseDto> createUaapSeason(@Valid @RequestBody UaapSeasonDto uaapSeasonDto) {
        uaapDataService.saveUaapSeason(uaapSeasonDto);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new ResponseDto(UaapDataConstants.STATUS_201, UaapDataConstants.MESSAGE_201));
    }

    @GetMapping("/fetch/uaapseason")
    public ResponseEntity<List<UaapSeason>> fetchAllUaapSeason() {
        List<UaapSeason> uaapSeasons = uaapDataService.findAllUaapSeason();

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(uaapSeasons);
    }

    @GetMapping("/fetch/uaapseason/{seasonId}")
    public ResponseEntity<UaapSeason> fetchUaapSeasonById(@PathVariable String seasonId) {
        UaapSeason uaapSeason = uaapDataService.findUaapSeasonById(seasonId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(uaapSeason);
    }

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

    @PutMapping("/update/uaapseason")
    public ResponseEntity<ResponseDto> updateUaapSeason(@Valid @RequestBody UaapSeasonDto uaapSeasonDto) {
        uaapDataService.updateUaapSeason(uaapSeasonDto);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ResponseDto(UaapDataConstants.STATUS_200, UaapDataConstants.MESSAGE_200));
    }




    @GetMapping("/fetch/uaapgame/{gameId}")
    public ResponseEntity<UaapGame> fetchUaapSeason(@PathVariable String gameId) {
        UaapGame game = uaapDataService.findUaapGameById(gameId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(game);
    }

    @PostMapping("/create/uaapgame")
    public ResponseEntity<ResponseDto> createUaapGame(@RequestBody UaapGameDto uaapGameDto) {
        uaapDataService.saveUaapGame(uaapGameDto);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new ResponseDto(UaapDataConstants.STATUS_201, UaapDataConstants.MESSAGE_201));
    }

    @PutMapping("/update/uaapgame")
    public ResponseEntity<ResponseDto> updateUaapGame(@RequestBody UaapGameDto uaapGameDto) {
        uaapDataService.updateUaapGame(uaapGameDto);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ResponseDto(UaapDataConstants.STATUS_201, UaapDataConstants.MESSAGE_201));
    }

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



    @GetMapping(value = "/fetch/uaapstats", params = "code=BB")
    public ResponseEntity<List<? extends PlayerStat>> fetchUaapStatsBball(@RequestParam String gameId) {
        List<? extends PlayerStat> uaapStats = uaapDataService.findUaapStatsByGameId(gameId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(uaapStats);
    }

    @GetMapping(value = "/fetch/uaapstats", params = "code=VB")
    public ResponseEntity<List<? extends PlayerStat>> fetchUaapStatsVball(@RequestParam String gameId) {
        List<? extends PlayerStat> uaapStats = uaapDataService.findUaapStatsByGameId(gameId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(uaapStats);
    }

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
