package com.teamr.runardo.uaapstatsdata.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "uaap_game_results")
@Data
public class GameResult {
    @Id //<gameID-univCode>
    @Column(name = "id")
    private String id;

    @Column(name = "game_id")
    private String gameId;

    @ManyToOne()
    @JoinColumn(name = "univ_id")
    private UaapUniv univ;

    @Column(name = "team_tag")
    private String teamTag;

    @Column(name = "final_score")
    private int finalScore;
}
