package com.teamr.runardo.uaapstatscraper.dto.playerstat;

import com.teamr.runardo.uaapstatscraper.dto.Player;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Builder
@Schema(
        name = "Volleybal player stat",
        description = "Schema volleyball player stat"
)
@Data
public class VballPlayerStat implements PlayerStat{
    private Player player;

    @Schema(description = "Game Result ID <seasonNum-gameCode-gameNum-univCode>", example = "87-MBB-1-UP")
    private String gameResultId;

    @Schema(description = "Number of attack attempts", example = "14")
    private Integer attackAttempt;

    @Schema(description = "Number of attack made", example = "10")
    private Integer attackMade;

    @Schema(description = "Number of serve attempts", example = "2")
    private Integer serveAttempt;

    @Schema(description = "Number of serve made", example = "10")
    private Integer serveMade;

    @Schema(description = "Number of block attempts", example = "10")
    private Integer blockAttempt;

    @Schema(description = "Number of block made", example = "3")
    private Integer blockMade;

    @Schema(description = "Number of dig attempts", example = "10")
    private Integer digAttempt;

    @Schema(description = "Number of dig made", example = "5")
    private Integer digMade;

    @Schema(description = "Number of receive attempts", example = "8")
    private Integer receiveAttempt;

    @Schema(description = "Number of receive made", example = "2")
    private Integer receiveMade;

    @Schema(description = "Number of set attempts", example = "8")
    private Integer setAttempt;

    @Schema(description = "Number of set made", example = "2")
    private Integer setMade;
}
