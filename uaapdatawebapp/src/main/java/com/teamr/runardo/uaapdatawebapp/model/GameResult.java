package com.teamr.runardo.uaapdatawebapp.model;

import lombok.Data;

import java.util.Objects;

@Data
public class GameResult {
    private String id;

    private String gameId;

    private UaapUniv univ;

    private String teamTag;

    private int finalScore;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GameResult that = (GameResult) o;
        return finalScore == that.finalScore && Objects.equals(id, that.id) && Objects.equals(univ, that.univ) && Objects.equals(teamTag, that.teamTag);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, univ, teamTag, finalScore);
    }
}
