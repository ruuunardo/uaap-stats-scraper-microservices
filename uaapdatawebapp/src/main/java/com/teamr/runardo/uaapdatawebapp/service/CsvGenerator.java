package com.teamr.runardo.uaapdatawebapp.service;

import com.teamr.runardo.uaapdatawebapp.model.BballPlayerStat;
import com.teamr.runardo.uaapdatawebapp.model.PlayerStat;
import com.teamr.runardo.uaapdatawebapp.model.VballPlayerStat;
import com.teamr.runardo.uaapdatawebapp.model.dto.GameResultDto;
import com.teamr.runardo.uaapdatawebapp.model.dto.UaapGameDto;
import com.teamr.runardo.uaapdatawebapp.model.dto.UaapSeasonDto;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

public class CsvGenerator {

    private final CSVPrinter printer;

    public CsvGenerator(Writer writer) throws IOException {
        this.printer = new CSVPrinter(writer, CSVFormat.EXCEL);
    }

    public void writeUaapGamesToCsv(List<UaapGameDto> uaapGames, UaapSeasonDto uaapSeason) throws IOException {
        printer.printRecord(generateGameHeader());
        for (UaapGameDto game : uaapGames) {
            int i = 0;
            for (GameResultDto gameResult : game.getGameResultDtos()) {
                GameResultDto oppGameResult = i == 0 ? game.getGameResultDtos().get(1) : game.getGameResultDtos().get(0);
                printer.printRecord(generateGameData(game, uaapSeason, oppGameResult, gameResult));
                ++i;
            }
        }
    }


    private List<String> generateGameHeader() {
        return List.of("SEASON_NUM", "GAME_CODE", "GAME_NUM", "GAME_RESULT_ID", "TEAM_TAG", "TEAM_NAME", "SCORE", "OPPONENT", "OPPONENT_TEAM_SCORE");
    }

    private List<String> generateGameData(UaapGameDto game, UaapSeasonDto uaapSeason, GameResultDto oppGameResult, GameResultDto gameResult) throws IOException {
        List<String> data = new ArrayList<>();
        data.add(String.valueOf(uaapSeason.getSeasonNumber()));
        data.add(uaapSeason.getGameCode().getGameCode());
        data.add(String.valueOf(game.getGameNumber()));
        data.add(gameResult.getId());
        data.add(gameResult.getTeamTag());
        data.add(gameResult.getUniv().getUnivCode());
        data.add(String.valueOf(gameResult.getFinalScore()));
        data.add(oppGameResult.getUniv().getUnivCode());
        data.add(String.valueOf(oppGameResult.getFinalScore()));

        return data;
    }

//    ---------------------------------------------------------------
    public void writeUaapGamesToCsv(List<? extends PlayerStat> playerStats, String seasonId, String code) throws IOException {
        String gameCode = seasonId.split("-")[1];
        printer.printRecord(generateStatsHeader(gameCode));
        for (PlayerStat p : playerStats) {
            printer.printRecord(generateStatsData(gameCode, p));
        }

    }

    private List<String> generateStatsHeader(String gameCode) {
        List<String> header = new ArrayList<>();

        if (gameCode.endsWith("BB")) {
            header = List.of("GAME_RESULT_ID", "PLAYER_ID", "PLAYER_NAME"
                    , "MIN_PLAYED", "FIELD_GOAL_ATTEMPTS", "FIELD_GOAL_MADE", "TWO_POINTS_ATTEMPTS", "TWO_POINTS_MADE", "THREE_POINTS_ATTEMPTS", "THREE_POINTS_MADE", "FREE_THROW_ATTEMPTS", "FREE_THROW_MADE"
                    , "REBOUNDS_OR", "REBOUNDS_DR", "ASSIST", "TURN_OVER", "STEAL", "BLOCK", "FOULS_PF", "FOULS_FD", "EFFICIENCY", "POINTS", "IS_FIRST_FIVE");
        } else if (gameCode.endsWith("VB")) {
            header = List.of("GAME_RESULT_ID", "PLAYER_ID", "PLAYER_NAME"
                    , "ATTACK_ATTEMPT", "ATTACK_MADE", "SERVE_ATTEMPT", "SERVE_MADE", "BLOCK_ATTEMPT", "BLOCK_MADE", "DIG_ATTEMPT", "DIG_MADE", "RECEIVE_ATTEMPT", "RECEIVE_MADE", "SET_ATTEMPT", "SET_MADE");
        }
        return header;
    }

    public List<String> generateStatsData(String gameCode, PlayerStat playerStat) {
        List<String> data = new ArrayList<>();

        if (gameCode.endsWith("BB")) {
            BballPlayerStat stat = (BballPlayerStat) playerStat;
//            data.add(stat.getGameResultId());
            data.add(stat.getPlayer().getId());
            data.add(stat.getPlayer().getName());
//            data.add(String.valueOf(stat.getMinPlayed()));
            data.add(String.valueOf(stat.getFieldGoalAttempts()));
            data.add(String.valueOf(stat.getFieldGoalMade()));
            data.add(String.valueOf(stat.getTwoPointsAttempts()));
            data.add(String.valueOf(stat.getTwoPointsMade()));
            data.add(String.valueOf(stat.getThreePointsAttempts()));
            data.add(String.valueOf(stat.getThreePointsMade()));
            data.add(String.valueOf(stat.getFreeThrowAttempts()));
            data.add(String.valueOf(stat.getFreeThrowMade()));
            data.add(String.valueOf(stat.getReboundsOR()));
            data.add(String.valueOf(stat.getReboundsDR()));
            data.add(String.valueOf(stat.getAssist()));
            data.add(String.valueOf(stat.getTurnOver()));
            data.add(String.valueOf(stat.getSteal()));
            data.add(String.valueOf(stat.getBlock()));
            data.add(String.valueOf(stat.getFoulsPF()));
            data.add(String.valueOf(stat.getFoulsFD()));
            data.add(String.valueOf(stat.getEfficiency()));
            data.add(String.valueOf(stat.getPoints()));
            data.add(String.valueOf(stat.getIsFirstFive()));

        } else if (gameCode.endsWith("VB")) {
            VballPlayerStat stat = (VballPlayerStat) playerStat;
//            data.add(stat.getGameResultId());
            data.add(stat.getPlayer().getId());
            data.add(stat.getPlayer().getName());
            data.add(String.valueOf(stat.getAttackMade()));
            data.add(String.valueOf(stat.getAttackAttempt()));
        }
        return data;
    }


}
