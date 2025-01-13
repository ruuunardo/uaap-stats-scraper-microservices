package com.teamr.runardo.uaapdatawebapp.service;

import com.teamr.runardo.uaapdatawebapp.mapper.UaapGameMapper;
import com.teamr.runardo.uaapdatawebapp.mapper.UaapSeasonMapper;
import com.teamr.runardo.uaapdatawebapp.model.PlayerStat;
import com.teamr.runardo.uaapdatawebapp.model.UaapGame;
import com.teamr.runardo.uaapdatawebapp.model.UaapSeason;
import com.teamr.runardo.uaapdatawebapp.model.dto.UaapGameDto;
import com.teamr.runardo.uaapdatawebapp.model.dto.UaapSeasonDto;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.stream.Stream;

@Service
@NoArgsConstructor
public class FileService {

    public List<UaapSeasonDto> getUaapSeasonList(MultipartFile csvFile) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(csvFile.getInputStream()));

        return bufferedReader.lines()
                .skip(1)
                .map(UaapSeasonDto::parse)
                .toList();
    }

    public void generateCSV(HttpServletResponse response, String seasonId, List<? extends PlayerStat> playerStats) throws IOException {
        CsvGenerator csvGenerator = new CsvGenerator(response.getWriter());
        csvGenerator.writeUaapGamesToCsv(playerStats, seasonId, "STATS");
    }

    public void generateCSV(HttpServletResponse response, UaapSeason season) throws IOException {
        List<UaapGame> uaapGames = season.getUaapGames();
        List<UaapGameDto> uaapGameDtos = uaapGames.stream().map(
                g -> {
                    return UaapGameMapper.mapToUaapGameDto(g, new UaapGameDto());
                }
        ).toList();

        CsvGenerator csvGenerator = new CsvGenerator(response.getWriter());
        csvGenerator.writeUaapGamesToCsv(uaapGameDtos, UaapSeasonMapper.mapToUaapSeasonDto(season, new UaapSeasonDto()));
    }
}
