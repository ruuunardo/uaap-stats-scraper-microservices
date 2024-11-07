package com.teamr.runardo.uaapstatscraper.dto.playerstat.factory;

import com.teamr.runardo.uaapstatscraper.dto.GameResultDto;
import com.teamr.runardo.uaapstatscraper.dto.playerstat.PlayerStat;

import java.util.Optional;

public interface PlayerStatsFactory {
    Optional<PlayerStat> parse(GameResultDto gameResultDto, String playerStatStr);

    /**
     * Generate stats id (SeasonNumber-GameCode-UnivCode-Number)
     * @param gameResultDto
     * @param playerNumber
     * @return
     */
    default String generatePlayerId(GameResultDto gameResultDto, String playerNumber) {
        String[] split = gameResultDto.getId().split("-");
        return String.join("-", split[0], split[1], split[3], playerNumber);
    }
}
