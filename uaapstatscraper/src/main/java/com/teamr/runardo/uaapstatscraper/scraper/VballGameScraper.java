package com.teamr.runardo.uaapstatscraper.scraper;

import com.teamr.runardo.uaapstatscraper.dto.GameResult;
import com.teamr.runardo.uaapstatscraper.dto.UaapGame;
import com.teamr.runardo.uaapstatscraper.dto.UaapSeason;
import com.teamr.runardo.uaapstatscraper.dto.UaapTeam;
import com.teamr.runardo.uaapstatscraper.dto.playerstat.PlayerStat;
import com.teamr.runardo.uaapstatscraper.dto.playerstat.factory.PlayerStatsFactory;
import com.teamr.runardo.uaapstatscraper.dto.playerstat.factory.VballPlayerStatFactory;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class VballGameScraper extends GameScraper{
    public VballGameScraper(UaapSeason uaapSeason) throws IOException {
        super(uaapSeason);
    }

    @Override
    protected Elements getGameSchedElements(Document gameDoc) {
        // get elements with the game data
        String gameSchedQuery = "a.schedule-box";      //anchor tag with "game-score class"
        Elements gameScheds = gameDoc.select(gameSchedQuery);
        return gameScheds;
    }

    @Override
    protected UaapGame getScrapeGame(Element gameSched) {
        int gameNum = Integer.parseInt(gameSched.attr("href").split("/+")[4]);  //"match 1" get match number 1

        UaapGame uaapGameDto = new UaapGame();
        uaapGameDto.setGameNumber(gameNum);
        return uaapGameDto;
    }

    @Override
    protected void mapGameResultsToGame(Element gameSched, UaapGame game) {
        Elements schedTeams = gameSched.select(".scheduled-team");

        GameResult homeTeam = extractGameResult(game, schedTeams, "HOME");
        GameResult awayTeam = extractGameResult(game, schedTeams, "AWAY");

        game.setGameResults(List.of(homeTeam, awayTeam));
    }

    private GameResult extractGameResult(UaapGame scrapeGame, Elements schedTeams, String teamTag) {
        int index = "HOME".equals(teamTag) ? 0 : 1;
        String data = schedTeams.get(index).text().replaceAll("\\s+", "").toUpperCase();
        String[] splitStr = data.split("(?<=\\D)(?=\\d)");

        UaapTeam uaapTeam = UaapTeam.parse(splitStr[0]);
        int finalScore = Integer.parseInt(splitStr[1]);

        GameResult build = new GameResult.GameResultDtoBuilder()
                .setTeamTag(teamTag)
                .setFinalScore(finalScore)
                .setUniv(UaapTeam.uaapUnivFactory(uaapTeam))
                .setId(scrapeGame, uaapSeason)
                .build();
        return build;
    }

    @Override
    public void addAdditionalGameData(Document doc, UaapGame game) {
        Elements elements = doc.select("div#game-details div.game-detail span");
        String venue = elements.get(1).text();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yy hh:mm a");
        String textDate = elements.get(2).text();
        LocalDateTime gameDateLDT = LocalDateTime.parse(textDate, formatter);

        game.setVenue(venue);
        game.setGameSched(gameDateLDT);

    }
    @Override
    protected Elements getTableBodyElements(Document gameDocument) {
        String cssQuery = "div.boxscorewrap table.box-score tbody:not(tbody:has(.team-totals))";
        Elements elements = gameDocument.select(cssQuery);
        return elements;
    }

    @Override
    protected List<PlayerStat> getPlayerStatList(List<String> playerStatCsvList, GameResult gameResult, PlayerStatsFactory playerStatsFactory) {
        List<PlayerStat> playerStatList = new ArrayList<>();

        for (int i = 1; i < playerStatCsvList.size(); ++i) {
            String lineData = playerStatCsvList.get(i);

            PlayerStat playerStat = playerStatsFactory.parse(gameResult, lineData).orElseThrow(
                    () -> new RuntimeException("Cannot parse player data")
            );

            playerStatList.add(playerStat);
        }
        return playerStatList;
    }


    @Override
    protected PlayerStatsFactory getPlayerStatsFactory() {
        return new VballPlayerStatFactory();
    }

    @Override
    public HashMap<Integer, Integer> extractGameNumGameIdMap() {
        //get game schedule elements from Document
        Elements gameScheds = getGameSchedElements(document);

        HashMap<Integer, Integer> webGameIdMap = new HashMap<>();

        for (Element gameSched : gameScheds) {
            String[] split = gameSched.attr("href").split("/+");    //get link and split
            int index = 4;
            int webGameId = Integer.parseInt(split[index]);

            webGameIdMap.put(webGameId, webGameId); //same id
        }
        return  webGameIdMap;
    }
}
