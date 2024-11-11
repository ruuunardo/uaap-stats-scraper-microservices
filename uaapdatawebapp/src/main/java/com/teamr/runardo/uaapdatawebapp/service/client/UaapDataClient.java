package com.teamr.runardo.uaapdatawebapp.service.client;

import com.teamr.runardo.uaapdatawebapp.model.*;
import com.teamr.runardo.uaapdatawebapp.model.dto.ResponseDto;
import com.teamr.runardo.uaapdatawebapp.model.dto.UaapGameDto;
import com.teamr.runardo.uaapdatawebapp.model.dto.UaapSeasonDto;
import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient("uaapstatsdata")
public interface UaapDataClient {

    @GetMapping(value = "api/fetch/uaapseason", consumes = "application/json")
    public ResponseEntity<List<UaapSeason>> fetchAllUaapSeason();

    @PostMapping(value = "api/create/uaapseason", consumes = "application/json")
    public ResponseEntity<ResponseDto> createUaapSeason(@RequestBody UaapSeasonDto uaapSeasonDto);

    @DeleteMapping(value = "api/delete/uaapseason", consumes = "application/json")
    public ResponseEntity<ResponseDto> deleteUaapSeasonById(@RequestParam String seasonId);

    @GetMapping("api/fetch/uaapseason/{seasonId}")
    public ResponseEntity<UaapSeason> fetchUaapSeasonById(@PathVariable String seasonId);

    @PutMapping("api/update/uaapseason")
    public ResponseEntity<ResponseDto> updateUaapSeason(@Valid @RequestBody UaapSeasonDto uaapSeasonDto);

    @DeleteMapping("api/delete/uaapgame")
    public ResponseEntity<ResponseDto> deleteUaapGameById(@RequestParam String gameId);

    @PostMapping("api/create/uaapgame")
    public ResponseEntity<UaapGame> createUaapGame(@RequestBody UaapGameDto uaapGameDto);

    @PutMapping("api/update/uaapgame")
    public ResponseEntity<ResponseDto> updateUaapGame(@RequestBody UaapGameDto uaapGameDto);


    @PostMapping(value = "api/create/uaapstats", params = "code=BB")
    public ResponseEntity<ResponseDto> createUaapStatsBball(@RequestBody List<BballPlayerStat> stat);

    @PostMapping(value = "api/create/uaapstats", params = "code=VB")
    public ResponseEntity<ResponseDto> createUaapStatsVball(@RequestBody List<VballPlayerStat> stat);

    @GetMapping(value = "api/fetch/uaapstats")
    public ResponseEntity<List<? extends PlayerStat>> fetchUaapStats(@RequestParam String gameId);

    }