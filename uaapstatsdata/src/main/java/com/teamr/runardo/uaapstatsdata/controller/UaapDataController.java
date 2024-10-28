package com.teamr.runardo.uaapstatsdata.controller;

import com.teamr.runardo.uaapstatsdata.constants.UaapDataConstants;
import com.teamr.runardo.uaapstatsdata.dto.ResponseDto;
import com.teamr.runardo.uaapstatsdata.dto.UaapSeasonDto;
import com.teamr.runardo.uaapstatsdata.entity.UaapSeason;
import com.teamr.runardo.uaapstatsdata.exception.UaapSeasonAlreadyExistsException;
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
        name = "CRUD REST APIs for  in EazyBank",
        description = "CRUD REST APIs in EazyBank to CREATE, UPDATE, FETCH AND DELETE account details"
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
        UaapSeason uaapSeason = new UaapSeason();
        UaapSeasonMapper.mapToUaapSeason(uaapSeasonDto, uaapSeason);

        Optional<UaapSeason> us = uaapDataService.findUaapSeasonBySeasonAndGameCode(uaapSeason.getSeasonNumber(), uaapSeason.getGameCode().getGameCode());
        if (us.isPresent()) {
            throw new UaapSeasonAlreadyExistsException(String.format("UAAP season already exist: %d-%s", uaapSeason.getSeasonNumber(), uaapSeason.getGameCode().getGameCode()) );
        }

        uaapDataService.saveUaapSeason(uaapSeason);
        log.info(uaapSeason.toString());

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new ResponseDto(UaapDataConstants.STATUS_201, UaapDataConstants.MESSAGE_201));
    }

    @GetMapping("/fetch/uaapseason")
    public ResponseEntity<List<UaapSeason>> fetchAllUaapSeason() {
        List<UaapSeason> uaapSeasons =  uaapDataService.findAllUaapSeason();
    }


    @DeleteMapping("/delete/uaapseason")
    public ResponseEntity<ResponseDto> deleteUaapSeason(@Valid @RequestBody UaapSeasonDto uaapSeasonDto){
        Boolean isDeleted = uaapDataService.deleteBySeasonAndGameCode(uaapSeasonDto.getSeasonNumber(), uaapSeasonDto.getGameCode().getGameCode());

        if (isDeleted) {
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new ResponseDto(UaapDataConstants.STATUS_200, UaapDataConstants.MESSAGE_200));
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseDto(UaapDataConstants.STATUS_417, UaapDataConstants.MESSAGE_417_DELETE));
        }
    }

    @GetMapping("/fetch/uaapseason")
    public ResponseEntity<ResponseDto> fetchUaapSeason(@RequestParam Integer gameNumber) {
        log.info("test");
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new ResponseDto(UaapDataConstants.STATUS_201, UaapDataConstants.MESSAGE_201));
    }
}
