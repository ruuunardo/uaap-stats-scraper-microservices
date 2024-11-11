package com.teamr.runardo.uaapstatsdata.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@MappedSuperclass
@IdClass(CompositeStatId.class)
@Getter
@Setter
@ToString
@Schema(discriminatorProperty = "type", description = "Base class for different types of player stats")
public abstract class PlayerStat {
    @Id
    @JoinColumn(name = "player_id")
    @ManyToOne(cascade = CascadeType.MERGE)
    @Schema(description = "Player object")
    private Player player;

    @Id
    @Column(name = "game_result_id")
    @Schema(description = "Game Result Id")
    private String gameResultId;
}
