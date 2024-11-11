package com.teamr.runardo.uaapstatscraper.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
@Schema(
        name = "Uaap Game Result",
        description = "Schema to hold UAAP Game Result information"
)
public class GameResultDto {
    @NotEmpty(message = "Id cannot be a null or empty")
    @Schema(description = "Game Result ID <seasonNum-gameCode-gameNum-univCode>", example = "87-MBB-1-UP")
    private String id;

    @Schema(description = "Univ details of the Game Results")
    private UaapUniv univ;

    @Schema(description = "Team tag of the Game Results")
    @Pattern(regexp = "^(AWAY|HOME)$", message = "Should only be AWAY or HOME")
    private String teamTag;

    @Schema(description = "Final score of the team")
    @Min(value = 0, message = "should be greater than 0")
    private int finalScore;

    public GameResultDto(GameResultDtoBuilder gameResultDtoBuilder) {
        this.id = gameResultDtoBuilder.id;
        this.univ = gameResultDtoBuilder.univ;
        this.teamTag = gameResultDtoBuilder.teamTag;
        this.finalScore = gameResultDtoBuilder.finalScore;
    }

    public static class GameResultDtoBuilder {

        private String id;
        private UaapUniv univ;
        private String teamTag;
        private int finalScore;

        public GameResultDtoBuilder() {
        }

        public GameResultDtoBuilder setId(UaapGameDto gameDto, UaapSeasonDto seasonDto) {
            this.id = String.join("-",
                    String.valueOf(seasonDto.getSeasonNumber()),
                    seasonDto.getGameCode().getGameCode(),
                    String.valueOf(gameDto.getGameNumber()),
                    univ.getUnivCode());
            return this;
        }

        public GameResultDtoBuilder setUniv(UaapUniv univ) {
            this.univ = univ;
            return this;
        }

        public GameResultDtoBuilder setTeamTag(String teamTag) {
            this.teamTag = teamTag;
            return this;
        }

        public GameResultDtoBuilder setFinalScore(int finalScore) {
            this.finalScore = finalScore;
            return this;
        }

        // Id created in build
        public GameResultDto build() {
            assert univ != null;
            assert id != null;

            return new GameResultDto(this);
        }
    }
}
