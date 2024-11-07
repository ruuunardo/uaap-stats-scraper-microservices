package com.teamr.runardo.uaapstatsdata.entity;

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
public abstract class PlayerStat {
    @Id
    @JoinColumn(name = "player_id")
    @ManyToOne(cascade = CascadeType.MERGE)
    private Player player;

    @Id
    @Column(name = "game_result_id")
    private String gameResultId;
}
