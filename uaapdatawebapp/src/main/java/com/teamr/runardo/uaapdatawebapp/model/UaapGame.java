package com.teamr.runardo.uaapdatawebapp.model;


import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Data
public class UaapGame implements Comparable<UaapGame> {
    private String id;

    private int gameNumber;

    private LocalDateTime gameSched;

    private String venue;

    private String seasonId;

    private List<GameResult> gameResults;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UaapGame uaapGame = (UaapGame) o;
        return gameNumber == uaapGame.gameNumber && Objects.equals(id, uaapGame.id) && Objects.equals(gameSched, uaapGame.gameSched) && Objects.equals(venue, uaapGame.venue) && Objects.equals(seasonId, uaapGame.seasonId) && Objects.equals(gameResults, uaapGame.gameResults);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, gameNumber, gameSched, venue, seasonId, gameResults);
    }


    @Override
    public int compareTo(UaapGame o) {
        return Integer.compare(this.gameNumber, o.gameNumber);
    }
}
