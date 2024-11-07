package com.teamr.runardo.uaapdatawebapp.model;

import lombok.Data;

import java.util.List;

@Data
public class UaapSeason {
//    "87-MBB"
    private String id;

    private int seasonNumber;

    private String url;

    private UaapGameCode gameCode;

    private boolean urlWork;

    private List<UaapGame> uaapGames;
}
