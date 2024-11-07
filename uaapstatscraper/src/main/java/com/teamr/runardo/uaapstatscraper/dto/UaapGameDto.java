package com.teamr.runardo.uaapstatscraper.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Schema(
        name = "Uaap Game",
        description = "Schema to hold UAAP Game information"
)
public class UaapGameDto {
    @Schema(
            description = "Game number based on order of games", example = "1"
    )
    @NotEmpty(message = "AccountNumber can not be a null or empty")
    @Min(value = 1, message = "Game number should be greater than 1")
    private int gameNumber;

    @Schema(
            description = "Game schedule (date and time)"
    )
    private LocalDateTime gameSched;

    @Schema(
            description = "Game venue of game", example = "MOA arena"
    )
    private String venue;

    private List<GameResultDto> gameResultDtos;

    @Schema(description = "Id of game season", example = "87-MBB" )
    @NotEmpty(message = "SeasonId can not be a null or empty")
    private String uaapSeasonId;

    @Schema(description = "URL for the data source", example = "https://uaap.livestats.ph/tournaments/uaap-season-87-men-s?game_id=:id")
    @Pattern(regexp="^(http)s?.*:id.*",message = "URL must have an 'id' parameter ")
    private String url;
}
