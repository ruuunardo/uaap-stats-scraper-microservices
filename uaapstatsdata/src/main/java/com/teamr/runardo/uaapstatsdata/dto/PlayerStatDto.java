package com.teamr.runardo.uaapstatsdata.dto;

import com.teamr.runardo.uaapstatsdata.entity.PlayerStat;
import lombok.Data;

@Data
public class PlayerStatDto <T>{
    private T data;
}
