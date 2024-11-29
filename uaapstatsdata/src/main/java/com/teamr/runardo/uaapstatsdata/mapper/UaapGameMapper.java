package com.teamr.runardo.uaapstatsdata.mapper;

import com.teamr.runardo.uaapstatsdata.dto.GameResultDto;
import com.teamr.runardo.uaapstatsdata.dto.UaapGameDto;
import com.teamr.runardo.uaapstatsdata.entity.GameResult;
import com.teamr.runardo.uaapstatsdata.entity.UaapGame;

import java.util.List;

public class UaapGameMapper {
    public static UaapGame mapToUaapGame(UaapGameDto uaapGameDto, UaapGame uaapGame) {
        uaapGame.setId(uaapGameDto.getUaapSeasonId().concat("-").concat(String.valueOf(uaapGameDto.getGameNumber())));
        uaapGame.setGameNumber(uaapGameDto.getGameNumber());
        uaapGame.setGameSched(uaapGameDto.getGameSched());
        uaapGame.setVenue(uaapGameDto.getVenue());
        uaapGame.setSeasonId(uaapGameDto.getUaapSeasonId());

        if (uaapGameDto.getGameResultDtos() != null) {
            List<GameResult> gameResultList = uaapGameDto.getGameResultDtos().stream().map(
                    g -> {
                        GameResult gameResult = GameResultMapper.mapToGameResult(g, new GameResult());
                        gameResult.setGameId(uaapGame.getId());
                        return gameResult;
                    }
            ).toList();
            uaapGame.setGameResults(gameResultList);
        }

        return uaapGame;
    }
}
