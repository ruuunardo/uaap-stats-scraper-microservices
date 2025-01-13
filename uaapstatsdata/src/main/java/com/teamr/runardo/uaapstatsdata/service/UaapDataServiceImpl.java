package com.teamr.runardo.uaapstatsdata.service;

import com.teamr.runardo.uaapstatsdata.dto.UaapGameDto;
import com.teamr.runardo.uaapstatsdata.dto.UaapSeasonDto;
import com.teamr.runardo.uaapstatsdata.entity.*;
import com.teamr.runardo.uaapstatsdata.exception.ResourceNotFoundException;
import com.teamr.runardo.uaapstatsdata.exception.UaapSeasonAlreadyExistsException;
import com.teamr.runardo.uaapstatsdata.mapper.UaapGameMapper;
import com.teamr.runardo.uaapstatsdata.mapper.UaapSeasonMapper;
import com.teamr.runardo.uaapstatsdata.repository.*;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@AllArgsConstructor
@Transactional
public class UaapDataServiceImpl implements UaapDataService{
    private UaapSeasonRepository uaapSeasonRepository;
    private UaapGameRepository uaapGameRepository;
    private BBallPlayerStatRepository bBallPlayerStatRepository;
    private VBallPlayerStatRepository vBallPlayerStatRepository;
    private PlayerRepository playerRepository;

    @Override
    public UaapSeason saveUaapSeason(UaapSeasonDto uaapSeasonDto) {
        UaapSeason uaapSeason = new UaapSeason();
        UaapSeasonMapper.mapToUaapSeason(uaapSeasonDto, uaapSeason);
        return uaapSeasonRepository.customSaveGame(uaapSeason);
    }

    @Override
    public List<UaapSeason> findAllUaapSeason() {
        List<UaapSeason> uaapSeasons = uaapSeasonRepository.findAll();

        return uaapSeasons;
    }

    @Override
    public UaapSeason findUaapSeasonById(String seasonId) {
        UaapSeason season = uaapSeasonRepository.findById(seasonId).orElseThrow(
                () ->  new ResourceNotFoundException("UaapSeason", "id", seasonId)
        );

        return season;
    }

    @Override
    public Boolean deleteUaapSeasonById(String seasonId) {
        boolean isDelete = false;

        UaapSeason uaapSeason = findUaapSeasonById(seasonId);

        deleteUaapStatsByGameId(seasonId);
        uaapSeasonRepository.delete(uaapSeason);
        playerRepository.deleteAllBySeasonId(seasonId);
        isDelete = true;

        return isDelete;
    }


    @Override
    public Boolean updateUaapSeason(UaapSeasonDto uaapSeasonDto) {
        Boolean isUpdated = false;

        String id = String.join("-", String.valueOf(uaapSeasonDto.getSeasonNumber()), uaapSeasonDto.getGameCode().getGameCode());
        UaapSeason uaapSeason = findUaapSeasonById(id);
        UaapSeasonMapper.mapToUaapSeason(uaapSeasonDto, uaapSeason);
        uaapSeasonRepository.customSaveGame(uaapSeason);

        isUpdated = true;

        return isUpdated;
    }



    @Override
    public UaapGame saveUaapGame(UaapGameDto uaapGameDto) {
        uaapSeasonRepository.findById(uaapGameDto.getUaapSeasonId()).orElseThrow(
                () -> new ResourceNotFoundException(UaapSeason.class.toString(), "id", "uaapGameDto.getUaapSeasonId()")
        );

        UaapGame uaapGame = new UaapGame();
        UaapGameMapper.mapToUaapGame(uaapGameDto, uaapGame);

        return uaapGameRepository.save(uaapGame);
    }

    @Override
    public Boolean updateUaapGame(UaapGameDto uaapGameDto) {
        UaapGame uaapGame = new UaapGame();
        UaapGameMapper.mapToUaapGame(uaapGameDto, uaapGame);

        Boolean isUpdated = false;

        uaapGameRepository.save(uaapGame);

        isUpdated = true;

        return isUpdated;
    }

    @Override
    public UaapGame findUaapGameById(String gameId) {
        UaapGame game = uaapGameRepository.findById(gameId).orElseThrow(
                () ->  new ResourceNotFoundException("UaapGame", "id", gameId)
        );

        return game;
    }

    @Override
    public Boolean deleteUaapGameById(String gameId) {
        boolean isDelete = false;

        UaapGame game = findUaapGameById(gameId);
        deleteUaapStatsByGameId(gameId);
        uaapGameRepository.delete(game);

        isDelete = true;

        return isDelete;
    }

    @Override
    public <T extends PlayerStat> PlayerStat saveUaapStats(T stat) {
        if (stat instanceof BballPlayerStat) {
            BballPlayerStat saved = bBallPlayerStatRepository.save((BballPlayerStat) stat);
            return saved;
        } else if (stat instanceof VballPlayerStat) {
            VballPlayerStat saved = vBallPlayerStatRepository.save((VballPlayerStat) stat);
            return saved;
        } else {
            throw new RuntimeException("PlayerStat not supported");
        }

    }


    @Override
    public List<? extends PlayerStat> findUaapStats(String code) {
        if (Objects.equals(code, "BB")) {
            return bBallPlayerStatRepository.findAll();
        } else if (Objects.equals(code, "VB")) {
            return vBallPlayerStatRepository.findAll();
        } else {
            throw new RuntimeException("PlayerStat code not supported: " + code);
        }
    }

    @Override
    public <T extends PlayerStat> void saveUaapStats(List<T> stat) {
        for (PlayerStat s : stat) {
            saveUaapStats(s);
        }
    }

    @Override
    public void deleteUaapStatsByGameId(String gameId) {
        String gameCode = gameId.split("-")[1];

        if (gameCode.endsWith("BB")) {
            bBallPlayerStatRepository.deleteAllByGameId(gameId);
        } else if (gameCode.endsWith("VB")) {
            vBallPlayerStatRepository.deleteAllByGameId(gameId);
        } else {
            throw new RuntimeException("GameCode not valid: " + gameCode);
        }

    }

    @Override
    public void deleteAllPlayersBySeasonId(String seasonId) {
        playerRepository.deleteAllBySeasonId(seasonId);
    }

    @Override
    public List<? extends PlayerStat> findUaapStatsByGameId(String gameId) {
        String gameCode = gameId.split("-")[1];
        if (gameCode.endsWith("BB")) {
            List<BballPlayerStat> stats = bBallPlayerStatRepository.findAllByGameId(gameId);
            return stats;
        } else if (gameCode.endsWith("VB")) {
            List<VballPlayerStat> stats = vBallPlayerStatRepository.findAllByGameId(gameId);
            return stats;
        } else {
            throw new RuntimeException("Game code not supported: " + gameCode.endsWith("BB"));
        }

    }
}
