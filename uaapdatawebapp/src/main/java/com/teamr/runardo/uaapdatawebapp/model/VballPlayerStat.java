package com.teamr.runardo.uaapdatawebapp.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(
        name = "Basketball player stat",
        description = "Schema basket ball player stat"
)
@Data
//@NoArgsConstructor
public class VballPlayerStat extends PlayerStat{
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
