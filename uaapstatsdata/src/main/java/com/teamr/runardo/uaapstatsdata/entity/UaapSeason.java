package com.teamr.runardo.uaapstatsdata.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.Objects;

@Entity
@Table(name="uaap_seasons")
@Data
public class UaapSeason {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "season_number")
    private int seasonNumber;

    @Column(name = "url")
    private String url;

    @ManyToOne(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinColumn(name = "game_code")
    private UaapGameCode gameCode;

    @Column(name = "is_url_working")
    private boolean urlWork;
}
