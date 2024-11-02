package com.teamr.runardo.uaapstatsdata.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "uaap_univ")
@Data
public class UaapUniv {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "univ_code")
    private String univCode;

    @Column(name = "name_univ")
    private String univName;

}
