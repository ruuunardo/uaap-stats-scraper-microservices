package com.teamr.runardo.uaapstatsdata.dto;

import com.teamr.runardo.uaapstatsdata.entity.UaapUniv;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
@Schema(
        name = "Uaap Game Result",
        description = "Schema to hold UAAP Game Result information"
)
public class GameResultDto {
    @NotEmpty(message = "Id cannot be a null or empty")
    @Schema(description = "Game Result ID <seasonNum-gameCode-gameNum-univCode>", example = "87-MBB-1-UP")
    private String id;

    @Schema(description = "Univ details of the Game Results")
    private UaapUniv univ;

    @Schema(description = "Team tag of the Game Results")
    @Pattern(regexp = "", message = "Should only be AWAY or HOME")
    private String teamTag;

    @Schema(description = "Final score of the team")
    private int finalScore;

//    @Schema(description = "Player stats list")
//    private List<PlayerStat> playerStats;

}
