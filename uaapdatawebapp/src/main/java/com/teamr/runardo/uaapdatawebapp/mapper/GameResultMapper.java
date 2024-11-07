package com.teamr.runardo.uaapdatawebapp.mapper;


import com.teamr.runardo.uaapdatawebapp.model.GameResult;
import com.teamr.runardo.uaapdatawebapp.model.dto.GameResultDto;

public class GameResultMapper {
    public static GameResult mapToGameResult(GameResultDto gameResultDto, GameResult gameResult) {
        gameResult.setUniv(gameResultDto.getUniv());
        gameResult.setId(gameResultDto.getId());
        gameResult.setTeamTag(gameResultDto.getTeamTag());
        gameResult.setFinalScore(gameResultDto.getFinalScore());

        return gameResult;
    }

    public static GameResultDto mapToGameResultDto(GameResult gameResult, GameResultDto gameResultDto) {

        gameResultDto.setUniv(gameResult.getUniv());
        gameResultDto.setId(gameResult.getId());
        gameResultDto.setTeamTag(gameResult.getTeamTag());
        gameResultDto.setFinalScore(gameResult.getFinalScore());

        return gameResultDto;
    }
}
