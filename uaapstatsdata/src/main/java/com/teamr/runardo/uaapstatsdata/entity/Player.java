package com.teamr.runardo.uaapstatsdata.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Entity
@Table(name = "uaap_players")
public class Player {
    @Id
    @Column(name = "id")
    @NotEmpty(message = "Player id must not be empty")
    private String id;

    @NotEmpty(message = "Player number must not be empty")
    private String name;

    @NotNull(message = "Univ id must not be empty")
    private int univId;
}
