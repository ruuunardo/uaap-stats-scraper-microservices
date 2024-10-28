package com.teamr.runardo.uaapstatsdata.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "uaap_game_codes")
@Data
@NoArgsConstructor
public class UaapGameCode {
    @Id
    @Column(name = "game_code")
    private String gameCode;

    @Column(name = "game_name")
    private String gameName;


    public UaapGameCode(String gameCode, String gameName) {
        this.gameName = gameName;
        this.gameCode = gameCode;
    }
}
