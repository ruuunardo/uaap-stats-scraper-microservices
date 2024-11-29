package com.teamr.runardo.uaapstatsdata.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.teamr.runardo.uaapstatsdata.dto.UaapGameDto;
import com.teamr.runardo.uaapstatsdata.dto.UaapSeasonDto;
import com.teamr.runardo.uaapstatsdata.entity.UaapGame;
import com.teamr.runardo.uaapstatsdata.entity.UaapGameCode;
import com.teamr.runardo.uaapstatsdata.repository.UaapGameRepository;
import com.teamr.runardo.uaapstatsdata.repository.UaapSeasonRepository;
import com.teamr.runardo.uaapstatsdata.service.UaapDataService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.junit.jupiter.api.Assertions.*;

@TestPropertySource("classpath:application.yml")
@SpringBootTest
@AutoConfigureMockMvc
@Sql(scripts = "/data.sql")
class UaapDataControllerTest {
    @Autowired
    private JdbcTemplate jdbc;

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    UaapSeasonRepository uaapSeasonRepository;

    @Autowired
    UaapGameRepository uaapGameRepository;

    @Autowired
    UaapDataService uaapDataService;

//    @BeforeAll
//
//    public static void setUapBeforeAll() {
//
//    }

    @Test
    public void createUaapSeasonHttpTest() throws Exception {

        UaapSeasonDto uaapSeasonDto = new UaapSeasonDto();
        uaapSeasonDto.setSeasonNumber(1);
        uaapSeasonDto.setUrl("http://testurl.com/gameId=:id");
        uaapSeasonDto.setGameCode(new UaapGameCode("MBB", "Men's Basketball"));

        assertTrue(uaapSeasonRepository.findAll().isEmpty());

        mockMvc.perform(MockMvcRequestBuilders.post("/api/create/uaapseason")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(uaapSeasonDto)
                        ))
                .andExpect(status().is2xxSuccessful());

        assertFalse(uaapSeasonRepository.findAll().isEmpty());
    }

    @Test
    @Sql(scripts = {"classpath:/data.sql", "classpath:/insertUaapSeason.sql", "classpath:/insertUaapGame.sql", "classpath:/insertStats.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void fetchUaapSeasonHttp() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/fetch/uaapseason")
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id").value("1-MBB"))
                .andExpect(status().isOk());
    }

    @Test
    @Sql(scripts = {"classpath:/data.sql", "classpath:/insertUaapSeason.sql", "classpath:/insertUaapGame.sql", "classpath:/insertStats.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void fetchUaapSeasonByIdHttp() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/fetch/uaapseason/{id}", "1-MBB")
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(jsonPath("$.id").value("1-MBB"))
                .andExpect(jsonPath("$.uaapGames", hasSize(1)))
                .andExpect(jsonPath("$.uaapGames[0].gameResults", hasSize(2)))
                .andExpect(status().isOk())
        ;
    }

    @Test
    @Sql(scripts = {"classpath:/data.sql", "classpath:/insertUaapSeason.sql", "classpath:/insertUaapGame.sql", "classpath:/insertStats.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void deleteUaapSeasonByIdHttp() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/delete/uaapseason")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("seasonId", "1-MBB")
                )
                .andExpect(status().isOk())
        ;

        assertFalse(uaapSeasonRepository.findById("1-MBB").isPresent());

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/delete/uaapseason")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("seasonId", "1-MBB")
                )
                .andExpect(status().is4xxClientError())
        ;
    }

    @Test
    @Sql(scripts = {"classpath:/data.sql", "classpath:/insertUaapSeason.sql", "classpath:/insertUaapGame.sql", "classpath:/insertStats.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void fetchUaapGameHttp() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/fetch/uaapgame/{gameId}", "1-MBB-1")
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(jsonPath("$.id").value("1-MBB-1"))
                .andExpect(status().isOk())
        ;
    }

    @Test
    public void createUaapGameHttp() throws Exception {
        UaapSeasonDto uaapSeasonDto = new UaapSeasonDto();
        uaapSeasonDto.setSeasonNumber(1);
        uaapSeasonDto.setUrl("testurl.com");
        uaapSeasonDto.setGameCode(new UaapGameCode("MBB", "Men's Basketball"));

        uaapDataService.saveUaapSeason(uaapSeasonDto);

        UaapGameDto uaapGameDto = new UaapGameDto();
        uaapGameDto.setUaapSeasonId("1-MBB");
        uaapGameDto.setGameNumber(1);
        uaapGameDto.setUrl("testurl.com");

        UaapGame game = uaapDataService.saveUaapGame(uaapGameDto);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/create/uaapgame")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(uaapGameDto))
                )
                .andExpect(jsonPath("$.id").value("1-MBB-1"))
                .andExpect(status().is2xxSuccessful())
        ;
    }

    @Test
    @Sql(scripts = {"classpath:/data.sql", "classpath:/insertUaapSeason.sql", "classpath:/insertUaapGame.sql", "classpath:/insertStats.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void deleteUaapGameByIdHttp() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/delete/uaapgame")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("gameId", "1-MBB-1")
                )
                .andExpect(status().isOk())
        ;

        assertFalse(uaapGameRepository.findById("1-MBB-1").isPresent());

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/delete/uaapseason")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("seasonId", "1-MBB-1")
                )
                .andExpect(status().is4xxClientError())
        ;
    }

    @Test
    @Sql(scripts = {"classpath:/data.sql", "classpath:/insertUaapSeason.sql", "classpath:/insertUaapGame.sql", "classpath:/insertStats.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void fetchStatsHttp() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/fetch/uaapstats")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("gameId", "1-MBB-1")
                )
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    @Sql(scripts = {"classpath:/data.sql", "classpath:/insertUaapSeason.sql", "classpath:/insertUaapGame.sql", "classpath:/insertStats.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void deleteStatsHttp() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/delete/uaapstats")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("gameId", "1-MBB-1")
                )
                .andExpect(status().isOk());
    }


    @AfterEach
    public void afterEach() {
        jdbc.execute("delete from basketball_player_stats");
        jdbc.execute("delete from uaap_game_results");
        jdbc.execute("delete from uaap_games");
        jdbc.execute("delete from uaap_seasons");
        jdbc.execute("delete from uaap_univ");
        jdbc.execute("delete from uaap_game_codes");
        jdbc.execute("delete from uaap_players");

    }
}