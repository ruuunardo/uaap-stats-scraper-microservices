package com.teamr.runardo.uaapstatscraper.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Data
@Schema(
        name = "UAAP University",
        description = "Schema to hold UAAP University information"
)
public class UaapUniv {

    @Schema(
            description = "Id for UAAP Univ", example = "1"
    )
    private int id;

    @Schema(
            description = "Univ Code for UAAP Univ", example = "UP"
    )
    private String univCode;

    @Schema(
            description = "Name of University", example = "University of the Philippines"
    )
    private String univName;

}