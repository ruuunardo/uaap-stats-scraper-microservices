package com.teamr.runardo.uaapstatscraper.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
@Schema(
        name = "Uaap Season",
        description = "Schema to hold UAAP Season Game information"
)
public class UaapSeasonDto {

    @Min(value = 1, message = "Season number should be a whole number (>0)")
    @Schema(
            description = "Season number of UAAP", example = "87"
    )
    private int seasonNumber;

    @Schema(
            description = "URL for the data source", example = "https://uaap.livestats.ph/tournaments/uaap-season-87-men-s?game_id=:id"
    )
    @Pattern(regexp="^(http)s?.*:id.*",message = "URL must have an 'id' parameter ")
    private String url;


    @NotNull(message = "Game code should not be null")
    private UaapGameCode gameCode;
}
