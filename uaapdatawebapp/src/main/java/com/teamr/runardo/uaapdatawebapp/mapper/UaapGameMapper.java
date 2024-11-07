package com.teamr.runardo.uaapdatawebapp.mapper;


import com.teamr.runardo.uaapdatawebapp.model.GameResult;
import com.teamr.runardo.uaapdatawebapp.model.UaapGame;
import com.teamr.runardo.uaapdatawebapp.model.dto.GameResultDto;
import com.teamr.runardo.uaapdatawebapp.model.dto.UaapGameDto;

import java.util.List;

public class UaapGameMapper {
    public static UaapGame mapToUaapGame(UaapGameDto uaapGameDto, UaapGame uaapGame) {
        uaapGame.setId(uaapGameDto.getUaapSeasonId().concat("-").concat(String.valueOf(uaapGameDto.getGameNumber())));
        uaapGame.setGameNumber(uaapGameDto.getGameNumber());
        uaapGame.setGameSched(uaapGameDto.getGameSched());
        uaapGame.setVenue(uaapGameDto.getVenue());
        uaapGame.setSeasonId(uaapGameDto.getUaapSeasonId());

        List<GameResult> gameResultList = uaapGameDto.getGameResultDtos().stream().map(
                g -> {
                    GameResult gameResult = GameResultMapper.mapToGameResult(g, new GameResult());
                    gameResult.setGameId(uaapGame.getId());
                    return gameResult;
                }
        ).toList();

        uaapGame.setGameResults(gameResultList);

        return uaapGame;
    }

    public static UaapGameDto mapToUaapGameDto(UaapGame uaapGame, UaapGameDto uaapGameDto) {
        uaapGameDto.setGameNumber(uaapGame.getGameNumber());
        uaapGameDto.setGameSched(uaapGame.getGameSched());
        uaapGameDto.setVenue(uaapGame.getVenue());
        uaapGameDto.setUaapSeasonId(uaapGame.getSeasonId());

        List<GameResultDto> gameResultList = uaapGame.getGameResults().stream().map(
                g -> {
                    return GameResultMapper.mapToGameResultDto(g, new GameResultDto());
                }
        ).toList();

        uaapGameDto.setGameResultDtos(gameResultList);

        return uaapGameDto;
    }
}
