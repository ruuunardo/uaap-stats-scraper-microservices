package com.teamr.runardo.uaapstatscraper.dto.playerstat.factory;

import com.teamr.runardo.uaapstatscraper.dto.GameResult;
import com.teamr.runardo.uaapstatscraper.dto.Player;
import com.teamr.runardo.uaapstatscraper.dto.playerstat.PlayerStat;
import com.teamr.runardo.uaapstatscraper.dto.playerstat.VballPlayerStat;

import java.util.Optional;

public class VballPlayerStatFactory implements PlayerStatsFactory {
    @Override
    public Optional<PlayerStat> parse(GameResult gameResult, String playerStatStr) {
        //    ""5,Kennedy Batas,19 / 38,2 / 13,1 / 15,5 / 14,0 / 0,0 / 2""

        //split inline stat
        String[] splitStat = playerStatStr.split(",\\s*");

        //extract player details
        int univId = gameResult.getUniv().getId();
        String playerNumber = splitStat[0];
        String playerName = splitStat[1];

        //create player
        Player extractedPlayer = Player.builder()
                .id(generateId(gameResult, playerNumber))
                .name(playerName)
                .univId(univId)
                .build();

        //create new player stat
        String regex = "\\s*/\\s*";
        VballPlayerStat playerStat = VballPlayerStat.builder()
                .player(extractedPlayer)
                .attackMade(Integer.parseInt(splitStat[2].split(regex)[0]))
                .attackAttempt(Integer.parseInt(splitStat[2].split(regex)[1]))
                .blockMade(Integer.parseInt(splitStat[3].split(regex)[0]))
                .blockAttempt(Integer.parseInt(splitStat[3].split(regex)[1]))
                .serveMade(Integer.parseInt(splitStat[4].split(regex)[0]))
                .serveAttempt(Integer.parseInt(splitStat[4].split(regex)[1]))
                .digMade(Integer.parseInt(splitStat[5].split(regex)[0]))
                .digAttempt(Integer.parseInt(splitStat[5].split(regex)[1]))
                .receiveMade(Integer.parseInt(splitStat[6].split(regex)[0]))
                .receiveAttempt(Integer.parseInt(splitStat[6].split(regex)[1]))
                .setMade(Integer.parseInt(splitStat[7].split(regex)[0]))
                .setAttempt(Integer.parseInt(splitStat[7].split(regex)[1]))
                .gameResultId(gameResult.getId())
                .build();

        if (playerStatStr.isBlank())
            return Optional.empty();

        return Optional.of(playerStat);
    }
}
