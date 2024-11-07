package com.teamr.runardo.uaapdatawebapp.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@Schema(
        name = "UAAP Player",
        description = "Schema to hold UAAP Player information"
)
public class Player {

    @NotEmpty(message = "Player id must not be empty")
    @Schema(description = "Player id <seasonNum-gameCode-univCode-playerNumber>", example = "87-MBB-UP-18")
    private String id;

    @NotEmpty(message = "Player number must not be empty")
    @Schema(description = "Player name", example = "Harold Alarcon")
    private String name;

    @NotNull(message = "Univ id must not be empty")
    @Schema(description = "Player univ id", example = "1")
    private int univId;
}
