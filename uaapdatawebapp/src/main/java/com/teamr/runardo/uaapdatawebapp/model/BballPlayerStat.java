package com.teamr.runardo.uaapdatawebapp.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Schema(
        name = "Basketball player stat",
        description = "Schema basket ball player stat"
)
@Data
//@NoArgsConstructor
public class BballPlayerStat extends PlayerStat{

    private Player player;
    //
    private String gameResultId;

    private Integer isFirstFive;

    private Integer points;

    private LocalTime minPlayed;

    private Integer fieldGoalMade;

    private Integer fieldGoalAttempts;

    private Integer twoPointsMade;

    private Integer twoPointsAttempts;

    private Integer threePointsMade;

    private Integer threePointsAttempts;

    private Integer freeThrowMade;

    private Integer freeThrowAttempts;

    private Integer reboundsOR;

    private Integer reboundsDR;

    private Integer assist;

    private Integer turnOver;

    private Integer steal;

    private Integer block;

    private Integer foulsPF;

    private Integer foulsFD;

    private Integer efficiency;

}
