package com.teamr.runardo.uaapstatsdata.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "volleyball_player_stats")
public class VballPlayerStat extends PlayerStat{
    @Column(name = "attack_attempt")
    private Integer attackAttempt;

    @Column(name = "attack_made")
    private Integer attackMade;

    @Column(name = "serve_attempt")
    private Integer serveAttempt;

    @Column(name = "serve_made")
    private Integer serveMade;

    @Column(name = "block_attempt")
    private Integer blockAttempt;

    @Column(name = "block_made")
    private Integer blockMade;

    @Column(name = "dig_attempt")
    private Integer digAttempt;

    @Column(name = "dig_made")
    private Integer digMade;

    @Column(name = "receive_attempt")
    private Integer receiveAttempt;

    @Column(name = "receive_made")
    private Integer receiveMade;

    @Column(name = "set_attempt")
    private Integer setAttempt;

    @Column(name = "set_made")
    private Integer setMade;

}
