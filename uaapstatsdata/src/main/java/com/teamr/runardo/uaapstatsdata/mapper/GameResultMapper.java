package com.teamr.runardo.uaapstatsdata.mapper;

import com.teamr.runardo.uaapstatsdata.dto.GameResultDto;
import com.teamr.runardo.uaapstatsdata.entity.GameResult;

public class GameResultMapper {
    public static GameResult mapToGameResult(GameResultDto gameResultDto, GameResult gameResult) {
        gameResult.setUniv(gameResultDto.getUniv());
        gameResult.setId(gameResultDto.getId());
        gameResult.setTeamTag(gameResultDto.getTeamTag());
        gameResult.setFinalScore(gameResultDto.getFinalScore());

        return gameResult;
    }
}
