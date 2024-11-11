package com.teamr.runardo.uaapstatsdata.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "uaap_univ")
@Data
@Schema(name = "UAAP University", description = "Schema to hold UAAP University information")
public class UaapUniv {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Id of university", example = "1")
    private int id;

    @Column(name = "univ_code")
    @Schema(description = "University code", example = "ADMU")
    private String univCode;

    @Schema(description = "University name", example = "Ateneo de Manila University")
    @Column(name = "name_univ")
    private String univName;

}
