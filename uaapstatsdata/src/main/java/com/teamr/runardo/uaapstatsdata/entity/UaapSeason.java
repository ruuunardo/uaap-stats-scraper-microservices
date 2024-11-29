package com.teamr.runardo.uaapstatsdata.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Table(name="uaap_seasons")
@Data
public class UaapSeason {
    @Id
    @Column(name = "id")
//    "87-MBB"
    private String id;

    @Column(name = "season_number")
    private int seasonNumber;

    @Column(name = "url")
    private String url;

    @ManyToOne(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinColumn(name = "game_code")
    private UaapGameCode gameCode;

    @Column(name = "is_url_working")
    private Boolean urlWork;

    @OneToMany(cascade = {CascadeType.REMOVE}, fetch = FetchType.LAZY)
    @JoinColumn(name = "season_id")
    private List<UaapGame> uaapGames;
}
