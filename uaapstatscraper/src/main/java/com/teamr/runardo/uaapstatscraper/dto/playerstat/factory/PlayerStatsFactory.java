package com.teamr.runardo.uaapstatscraper.dto.playerstat.factory;

import com.teamr.runardo.uaapstatscraper.dto.GameResult;
import com.teamr.runardo.uaapstatscraper.dto.playerstat.PlayerStat;

import java.util.Optional;

public interface PlayerStatsFactory {
    Optional<PlayerStat> parse(GameResult gameResult, String playerStatStr);

    /**
     * Generate stats in a form of season
     * @param gameResult
     * @param playerNumber
     * @return
     */
    default String generateId(GameResult gameResult, String playerNumber) {
        String[] split = gameResult.getId().split("-");
        return String.join("-", split[0], split[1], split[3], playerNumber);
    }
}
