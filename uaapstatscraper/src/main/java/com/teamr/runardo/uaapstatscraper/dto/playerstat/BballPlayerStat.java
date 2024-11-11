package com.teamr.runardo.uaapstatscraper.dto.playerstat;

import com.teamr.runardo.uaapstatscraper.dto.Player;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.time.LocalTime;

@Builder
@Schema(
        name = "Basketball player stat",
        description = "Schema for basketball player stat"
)
@Data
public class BballPlayerStat implements PlayerStat{
    private Player player;

    @Schema(description = "Game Result ID <seasonNum-gameCode-gameNum-univCode>", example = "87-MBB-1-UP")
    private String gameResultId;

    @Schema(description = "Tagging if included in first five", example = "True")
    private Boolean isFirstFive;

    @Schema(description = "Points earned of a player", example = "15")
    private Integer points;

    @Schema(description = "Total time played by a player", example = "00:15")
    private LocalTime minPlayed;

    @Schema(description = "Total successful shots made", example = "7")
    private Integer fieldGoalMade;

    @Schema(description = "Total attempts shots made", example = "14")
    private Integer fieldGoalAttempts;

    @Schema(description = "2-pointer successful shots made", example = "3")
    private Integer twoPointsMade;

    @Schema(description = "2-pointer attempts shots made", example = "5")
    private Integer twoPointsAttempts;

    @Schema(description = "3-pointer successful shots made", example = "2")
    private Integer threePointsMade;

    @Schema(description = "3-pointer attempts shots made", example = "5")
    private Integer threePointsAttempts;

    @Schema(description = "Free throw successful shots made", example = "2")
    private Integer freeThrowMade;

    @Schema(description = "Free throw attempts shots made", example = "4")
    private Integer freeThrowAttempts;

    @Schema(description = "Offensive rebounds", example = "3")
    private Integer reboundsOR;

    @Schema(description = "Defensive rebounds", example = "4")
    private Integer reboundsDR;

    @Schema(description = "Number of assist", example = "3")
    private Integer assist;

    @Schema(description = "Number of turnovers", example = "5")
    private Integer turnOver;

    @Schema(description = "Number of steals", example = "3")
    private Integer steal;

    @Schema(description = "Number of blocks", example = "2")
    private Integer block;

    @Schema(description = "Number of personal fouls", example = "4")
    private Integer foulsPF;

    @Schema(description = "Number of fouls defended", example = "3")
    private Integer foulsFD;

    @Schema(description = "Efficiency points", example = "-10")
    private Integer efficiency;
}
