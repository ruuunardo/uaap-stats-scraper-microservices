package com.teamr.runardo.uaapstatsdata.mapper;

import com.teamr.runardo.uaapstatsdata.dto.UaapSeasonDto;
import com.teamr.runardo.uaapstatsdata.entity.UaapSeason;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;

public class UaapSeasonMapper {
    public static UaapSeason mapToUaapSeason(UaapSeasonDto uaapSeasonDto, UaapSeason uaapSeason) {
        uaapSeason.setSeasonNumber(uaapSeasonDto.getSeasonNumber());
        uaapSeason.setUrl(uaapSeasonDto.getUrl());
        uaapSeason.setGameCode(uaapSeasonDto.getGameCode());

        try {
            URL url = new URL(uaapSeasonDto.getUrl());
            URLConnection connection = url.openConnection();
            connection.connect();
            uaapSeason.setUrlWork(true);
        } catch (IOException e) {
            uaapSeason.setUrlWork(false);
        }

        uaapSeason.setId(String.valueOf(uaapSeasonDto.getSeasonNumber()).concat("-").concat(uaapSeasonDto.getGameCode().getGameCode()));
        return uaapSeason;
    }

}
