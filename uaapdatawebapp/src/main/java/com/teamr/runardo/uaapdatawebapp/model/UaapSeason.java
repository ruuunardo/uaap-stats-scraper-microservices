package com.teamr.runardo.uaapdatawebapp.model;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.util.List;

@Data
public class UaapSeason {
//    "87-MBB"
    private String id;

    @NotNull(message = "Email is required")
    private int seasonNumber;

    @Pattern(regexp = "^(http)s?.*:id.*", message = "URL must have an 'id' parameter ")
    @NotNull(message = "Url is required")
    private String url;

    private UaapGameCode gameCode;

    private boolean urlWork;

    private List<UaapGame> uaapGames;


}
