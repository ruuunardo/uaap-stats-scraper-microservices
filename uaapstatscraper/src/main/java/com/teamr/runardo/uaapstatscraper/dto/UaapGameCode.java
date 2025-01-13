package com.teamr.runardo.uaapstatscraper.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@Schema(
        name = "Uaap Game Code",
        description = "Schema to hold UAAP Game Code information"
)
public class UaapGameCode {
    @Schema(
            description = "Code for UAAP games", example = "MBB"
    )
    private String gameCode;

    @Schema(
            description = "Name for UAAP games", example = "Men's Basketball"
    )
    private String gameName;
}
