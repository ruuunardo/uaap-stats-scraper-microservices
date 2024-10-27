package com.teamr.runardo.uaapstatscraper.dto.playerstat;

import com.teamr.runardo.uaapstatscraper.dto.Player;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Builder
@Schema(
        name = "Basketball player stat",
        description = "Schema basket ball player stat"
)
@Data
public class VballPlayerStat implements PlayerStat{
    private Player player;
    //
    private String gameResultId;

    private Integer attackAttempt;

    private Integer attackMade;

    private Integer serveAttempt;

    private Integer serveMade;

    private Integer blockAttempt;

    private Integer blockMade;

    private Integer digAttempt;

    private Integer digMade;

    private Integer receiveAttempt;

    private Integer receiveMade;

    private Integer setAttempt;

    private Integer setMade;
}
