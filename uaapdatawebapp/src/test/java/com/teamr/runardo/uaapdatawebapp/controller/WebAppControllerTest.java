package com.teamr.runardo.uaapdatawebapp.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.teamr.runardo.uaapdatawebapp.model.UaapGameCode;
import com.teamr.runardo.uaapdatawebapp.model.UaapSeason;
import com.teamr.runardo.uaapdatawebapp.service.FileService;
import com.teamr.runardo.uaapdatawebapp.service.WebAppService;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.ModelAndViewAssert;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.servlet.ModelAndView;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class WebAppControllerTest {
    @Autowired
    MockMvc mockMvc;

    @MockBean
    private FileService fileService;

    @MockBean
    private WebAppService webAppService;

    @Autowired
    private WebAppController webAppController;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    public void test() {
    }

    @Test
    public void getAllUaapSeasonsTest() throws Exception {
        UaapSeason uaapSeason = new UaapSeason();
        uaapSeason.setSeasonNumber(87);
        uaapSeason.setId("87-MBB");
        uaapSeason.setUrl("http");
        uaapSeason.setUaapGames(new ArrayList<>());
        uaapSeason.setGameCode(new UaapGameCode("MBB", "Men's Basketball"));

        List<UaapSeason> uaapSeasons = List.of(uaapSeason);

        when(webAppService.fetchAllUaapSeason()).thenReturn(uaapSeasons);

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/uaap-games")).andReturn();
        ModelAndView modelAndView = mvcResult.getModelAndView();
        Map<String, Object> model = modelAndView.getModel();
        List uaapSeasonsList = (List) model.get("uaapSeasons");

        assertEquals(1, uaapSeasonsList.size());
        ModelAndViewAssert.assertViewName(modelAndView, "uaap-season-list");

    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void showFormForAdd() throws Exception {
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/uaap-games/show-form"))
                .andExpect(status().isOk())
                .andReturn();

        ModelAndView modelAndView = mvcResult.getModelAndView();
        UaapSeason gameSeasonView = (UaapSeason) modelAndView.getModel().get("gameSeason");
        UaapSeason gameSeason = new UaapSeason();
        gameSeason.setGameCode(new UaapGameCode());
        assertEquals(gameSeason, gameSeasonView);

        ModelAndViewAssert.assertViewName(modelAndView, "uaap-game-form");
    }

    @Test
    @WithMockUser(roles = {"ADMIN", "USER"})
    public void saveUaapSeasonTest() throws Exception {
        String content = "id=87-MBB&seasonNumber=87&url=https%3A%2F%2Fuaap.livestats.ph%2Ftournaments%2Fuaap-season-87-men-s%3Fgame_id%3Aid&gameCode%2BgameCode=MBB&gameCode%2BgameName=Men%27s+Basketball&urlWork=false";
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/uaap-games/save")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .content(content)
                        .with(csrf())
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/uaap-games"))
                .andExpect(model().hasNoErrors())
                .andReturn();
    }

    @Test
    @WithMockUser(roles = {"ADMIN", "USER"})
    public void saveUaapSeasonTestWithError() throws Exception {
        String con = "id=87-MBB&seasonNumber=&url=https&gameCode%2BgameCode=MBB&gameCode%2BgameName=Men%27s+Basketball&urlWork=false";
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/uaap-games/save")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .content(con)
                        .with(csrf())
                )
                .andExpect(status().isOk())
                .andExpect(view().name("uaap-game-form"))
                .andExpect(model().attributeHasFieldErrors("gameSeason", "url", "seasonNumber"))
                .andExpect(model().errorCount(2))
                .andReturn();
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void deleteUaapSeasonGameTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/uaap-games/delete")
                            .param("gameSeasonId", "1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/uaap-games"))
        ;
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void editUaapSeasonTest() throws Exception {
        UaapSeason uaapSeason = new UaapSeason();
        uaapSeason.setSeasonNumber(87);
        uaapSeason.setId("87-MBB");
        uaapSeason.setUrl("http");
        uaapSeason.setUaapGames(new ArrayList<>());
        uaapSeason.setGameCode(new UaapGameCode("MBB", "Men's Basketball"));

        when(webAppService.findUaapSeasonById("87-MBB")).thenReturn(uaapSeason);

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/uaap-games/edit")
                        .param("gameSeasonId", "87-MBB"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("gameSeason", uaapSeason))
                .andReturn();

        ModelAndViewAssert.assertViewName(mvcResult.getModelAndView(), "uaap-game-form");

    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void getGamesTest() throws Exception {
        UaapSeason uaapSeason = new UaapSeason();
        uaapSeason.setSeasonNumber(87);
        uaapSeason.setId("87-MBB");
        uaapSeason.setUrl("http");
        uaapSeason.setUaapGames(new ArrayList<>());
        uaapSeason.setGameCode(new UaapGameCode("MBB", "Men's Basketball"));

        when(webAppService.findUaapSeasonById("87-MBB")).thenReturn(uaapSeason);

        mockMvc.perform(MockMvcRequestBuilders.get("/uaap-games/gamelist/{gameSeasonId}", "87-MBB"))
                .andExpect(status().isOk())
                .andExpect(view().name("uaap-game-table"))
                .andExpect(model().attribute("uaapSeason", uaapSeason))
        ;
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void deleteGames() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/uaap-games/gamelist/{gameSeasonId}", "87-MBB")
                        .param("selections", "87-MBB-1", "87-MBB-2")
                        .param("delete", "true")
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("87-MBB"))
                ;
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void saveFromFile() throws Exception {
        String csvContent = """
                GAME_CODE,GAME_NAME,SEASON_NUMBER,URL
                MBB,Men's Basketball,85,https://uaap.livestats.ph/tournaments/uaap-season-85-men-s?game_id=:id
                """;
        byte[] csvContentBytes = csvContent.getBytes(StandardCharsets.UTF_8);
        MockMultipartFile file = new MockMultipartFile("csvFile", "test.txt", "text/csv", csvContentBytes);

        mockMvc.perform(MockMvcRequestBuilders.multipart("/uaap-games/save-from-file")
                .file(file)
                .with(csrf())
        ).andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrl("/uaap-games"))
        ;
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void downloadUaapGameTest() throws Exception {
        UaapSeason uaapSeason = new UaapSeason();
        uaapSeason.setSeasonNumber(87);
        uaapSeason.setId("87-MBB");
        uaapSeason.setUrl("http");
        uaapSeason.setUaapGames(new ArrayList<>());
        uaapSeason.setGameCode(new UaapGameCode("MBB", "Men's Basketball"));

        when(webAppService.findUaapSeasonById("87-MBB")).thenReturn(uaapSeason);
        mockMvc.perform(MockMvcRequestBuilders.get("/uaap-games/export-to-csv")
                        .param("gameSeasonId", "87-MBB")
                )
            .andExpect(status().isOk())  // Ensure status is 200 OK
            .andExpect(content().contentType("text/csv"))  // Ensure the content type is "text/csv"
            .andExpect(header().string("Content-Disposition", "attachment; filename=\"uaap-games_MBB-87.csv\""))  ;
    }
}