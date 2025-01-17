package com.teamr.runardo.uaapdatawebapp.model.dto;

import com.teamr.runardo.uaapdatawebapp.model.UaapGameCode;
import com.teamr.runardo.uaapdatawebapp.model.UaapSeason;
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


    public static UaapSeasonDto parse(String csvLine) {
        String[] fields = csvLine.split(",\\s*");
        UaapGameCode uaapGameCode = new UaapGameCode(fields[0], fields[1]);
        UaapSeasonDto uaapSeasonDto = new UaapSeasonDto();

        uaapSeasonDto.setSeasonNumber(Integer.parseInt(fields[2]));
        uaapSeasonDto.setGameCode(uaapGameCode);
        uaapSeasonDto.setUrl(fields[3]);

        return uaapSeasonDto;
    }
}
