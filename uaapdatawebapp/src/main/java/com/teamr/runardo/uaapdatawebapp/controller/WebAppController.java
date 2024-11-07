package com.teamr.runardo.uaapdatawebapp.controller;

import com.teamr.runardo.uaapdatawebapp.model.PlayerStat;
import com.teamr.runardo.uaapdatawebapp.model.UaapGame;
import com.teamr.runardo.uaapdatawebapp.model.UaapGameCode;
import com.teamr.runardo.uaapdatawebapp.model.UaapSeason;
import com.teamr.runardo.uaapdatawebapp.model.dto.UaapGameDto;
import com.teamr.runardo.uaapdatawebapp.model.dto.UaapSeasonDto;
import com.teamr.runardo.uaapdatawebapp.service.FileService;
import com.teamr.runardo.uaapdatawebapp.service.WebAppService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/uaap-games")
@AllArgsConstructor
public class WebAppController {
    private static final Logger log = LoggerFactory.getLogger(WebAppController.class);

    private WebAppService webAppService;
    private FileService fileService;

    @GetMapping("/hello")
    public String sayHello() {
        return "hello";
    }

    @GetMapping("/home")
    public String mainpage() {
        return "homepage";
    }


    /** Show all the list of UAAP Season
     *
     * @param model
     * @return
     */
    @GetMapping()
    public String getAllUaapSeasons(Model model) {
        List<UaapSeason> allUaapSeasonGames = webAppService.fetchAllUaapSeason();
        model.addAttribute("uaapSeasons", allUaapSeasonGames);
        return "uaap-season-list";
    }


    //form for adding season
    @GetMapping("/show-form")
    public String showFormForAdd(Model model) {
        log.info("This is for the new UAAP season");
        UaapSeason uaapSeason = new UaapSeason();
        uaapSeason.setGameCode(new UaapGameCode());
        model.addAttribute("gameSeason", uaapSeason);
        return "uaap-game-form";
    }

    //save season
    @PostMapping("/save")
    public String saveUaapSeason(@Valid @ModelAttribute("gameSeason") UaapSeason uaapSeason, Errors errors) {
        if (!errors.hasErrors()) {
            webAppService.saveUaapSeason(uaapSeason);
            return "redirect:/uaap-games";
        }
        return "uaap-games-form";
    }

    //Delete UaapSeason
    @GetMapping("/delete")
    public String deleteUaapSeasonGame(@RequestParam("gameSeasonId") String seasonId) {
        webAppService.deleteUaapSeasonById(seasonId);
        return "redirect:/uaap-games";
    }


    //Edit season
    @GetMapping("/edit")
    public String editUaapSeason(@RequestParam("gameSeasonId") String id, Model model) {
        UaapSeason uaapSeason = webAppService.findUaapSeasonById(id);
        model.addAttribute("gameSeason", uaapSeason);
        return "uaap-game-form";
    }

    //Update game Season
    @GetMapping("/update")
    public String updateUaapGameSeason(@RequestParam("gameSeasonId") String id, Model model) {
        webAppService.updateUaapSeasonGamesById(id);
        return "redirect:/uaap-games";
    }

    //Get games and result
    @GetMapping("/gamelist/{gameSeasonId}")
    public String getGames(@PathVariable("gameSeasonId") String seasonId, Model model) {
        UaapSeason uaapSeason = webAppService.findUaapSeasonById(seasonId);
        model.addAttribute("uaapSeason", uaapSeason);
        return "uaap-game-table";
    }

    //Delete games and result
    @GetMapping(value = "/gamelist/{gameSeasonId}", params = "delete=true")
    public String deleteGames(@PathVariable("gameSeasonId") String seasonId, @RequestParam("selections") Optional<List<String>> selections) {
        webAppService.deleteUaapGamesByIds(selections);

        String path = "redirect:gameSeasonId";
        return path.replace("gameSeasonId", String.valueOf(seasonId));
    }

    //save season from file
    @PostMapping(value = "/save-from-file")
    public String saveUaapGames(@RequestParam MultipartFile csvFile) throws IOException {
        List<UaapSeasonDto> uaapSeasonList = fileService.getUaapSeasonList(csvFile);
        webAppService.saveUaapSeasonList(uaapSeasonList);
        return "redirect:/uaap-games";
    }

    //export to csv (selected Uaap Games)
    @GetMapping(value = "/gamelist/{gameSeasonId}", params = "download=true")
    public void downloadGameList(@PathVariable("gameSeasonId") String id, @RequestParam("selections") Optional<List<String>> selections, HttpServletResponse response) throws IOException {
        List<? extends PlayerStat> playerStats = webAppService.fetchUaapStatsByGameIds(selections, id);
        fileService.generateCSV(response, id, playerStats);
    }

    //export to csv (ALL Uaap Games)
    @GetMapping("/export-to-csv")
    public void downloadUaapGame(HttpServletResponse response, @RequestParam("gameSeasonId") String id) throws IOException {
        UaapSeason uaapSeason = webAppService.findUaapSeasonById(id);
        fileService.generateCSV(response, uaapSeason);
    }
}
