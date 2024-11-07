package com.teamr.runardo.uaapstatsdata.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;

import java.time.LocalTime;

@Data
@Entity
@Table(name = "basketball_player_stats")
public class BballPlayerStat extends PlayerStat{
    @Column(name = "is_first_five")
    private Integer isFirstFive;

    @Column(name = "points")
    private Integer points;

    @Column(name = "min_played")
    private LocalTime minPlayed;

    @Column(name = "field_goal_made")
    private Integer fieldGoalMade;

    @Column(name = "field_goal_attempt")
    private Integer fieldGoalAttempts;

    @Column(name = "two_points_made")
    private Integer twoPointsMade;

    @Column(name = "two_points_attempt")
    private Integer twoPointsAttempts;

    @Column(name = "three_points_made")
    private Integer threePointsMade;

    @Column(name = "three_points_attempt")
    private Integer threePointsAttempts;

    @Column(name = "free_throw_made")
    private Integer freeThrowMade;

    @Column(name = "free_throw_attempt")
    private Integer freeThrowAttempts;

    @Column(name = "rebound_or")
    private Integer reboundsOR;

    @Column(name = "rebound_dr")
    private Integer reboundsDR;

    @Column(name = "assist")
    private Integer assist;

    @Column(name = "turn_over")
    private Integer turnOver;

    @Column(name = "steal")
    private Integer steal;

    @Column(name = "block")
    private Integer block;

    @Column(name = "foul_pf")
    private Integer foulsPF;

    @Column(name = "foul_fd")
    private Integer foulsFD;

    @Column(name = "efficiency")
    private Integer efficiency;

}
