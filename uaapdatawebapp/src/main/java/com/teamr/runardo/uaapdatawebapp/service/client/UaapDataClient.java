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

@FeignClient(name = "uaapstatsdata", url = "http://uaapstatsdata:8080")
public interface UaapDataClient {

    @GetMapping(value = "api/uaapseasons", consumes = "application/json")
    public ResponseEntity<List<UaapSeason>> fetchAllUaapSeason();

    @PostMapping(value = "api/uaapseasons", consumes = "application/json")
    public ResponseEntity<ResponseDto> createUaapSeason(@RequestBody UaapSeasonDto uaapSeasonDto);

    @DeleteMapping(value = "api/uaapseasons/{seasonId}", consumes = "application/json")
    public ResponseEntity<ResponseDto> deleteUaapSeasonById(@PathVariable String seasonId);

    @GetMapping("api/uaapseasons/{seasonId}")
    public ResponseEntity<UaapSeason> fetchUaapSeasonById(@PathVariable String seasonId);

    @PutMapping("api/uaapseasons")
    public ResponseEntity<ResponseDto> updateUaapSeason(@Valid @RequestBody UaapSeasonDto uaapSeasonDto);

    @DeleteMapping("api/uaapgames/{gameId}")
    public ResponseEntity<ResponseDto> deleteUaapGameById(@PathVariable String gameId);

    @PostMapping("api/uaapgames")
    public ResponseEntity<UaapGame> createUaapGame(@RequestBody UaapGameDto uaapGameDto);

    @PutMapping("api/uaapgames")
    public ResponseEntity<ResponseDto> updateUaapGame(@RequestBody UaapGameDto uaapGameDto);

    @PostMapping(value = "api/uaapstats")
    public ResponseEntity<ResponseDto> createUaapStats(@RequestBody List<? extends PlayerStat> stat);

    @GetMapping(value = "api/uaapstats")
    public ResponseEntity<List<? extends PlayerStat>> fetchUaapStats(@RequestParam String gameId);

    }