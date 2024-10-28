package com.teamr.runardo.uaapstatsdata.entity;


import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "uaap_games")
@Data
public class UaapGame {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "game_number")
    private int gameNumber;

    @Column(name = "game_sched")
    private LocalDateTime gameSched;

    @Column(name = "game_venue")
    private String venue;

    @Column(name = "season_id")
    private int seasonId;

    @OneToMany(cascade = CascadeType.REMOVE
            , fetch = FetchType.EAGER
    )
    @JoinColumn(name="game_id")
    private List<GameResult> gameResults;
}
