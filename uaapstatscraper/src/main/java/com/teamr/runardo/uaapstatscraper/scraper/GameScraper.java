package com.teamr.runardo.uaapstatscraper.scraper;

import com.teamr.runardo.uaapstatscraper.dto.GameResult;
import com.teamr.runardo.uaapstatscraper.dto.UaapGame;
import com.teamr.runardo.uaapstatscraper.dto.UaapSeason;
import com.teamr.runardo.uaapstatscraper.dto.playerstat.PlayerStat;
import com.teamr.runardo.uaapstatscraper.dto.playerstat.factory.PlayerStatsFactory;
import com.teamr.runardo.uaapstatscraper.utility.UtilityClass;
import lombok.Getter;
import org.jsoup.HttpStatusException;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Getter
public abstract class GameScraper {
    protected Document document;
    protected UaapSeason uaapSeason;

    /** Set document(using the URL)
     *
     * @param uaapSeason season information of UAAP
     * @throws RuntimeException if can't connect to the server
     */
    protected GameScraper(UaapSeason uaapSeason) throws IOException {
        this.uaapSeason = uaapSeason;
        this.document = UtilityClass.getGameDocument(uaapSeason.getUrl());
    }

    protected GameScraper(){
    }

    /**
     * <h6>Scrapes all Uaap Games</h2>
     * <ol>
     *  <li>Get game schedule elements from Document for list of UAAP Games extraction</li>
     *  <li>Create {@code List} of {@link UaapGame}</li>
     *  <li>Iterate each element to extract each game data</li>
     *      <ol>
     *          <li>Get {@link UaapGame} game number</li>
     *          <li>Set {@code uaapSeasonId}({@link UaapSeason} {@code seasonNumber}-{@code gameCode})</li>
     *          <li>Extract {@link GameResult}({@code home} and {@code away} teams) and map to each games</li>
     *          <li>Add to created {@code List} of {@link UaapGame} </li>
     *      </ol>
     *  <li>Map additional data(venue and schedule) to each game</li>
     *
     * </ol>
     * @return {@code List} of {@link UaapGame}
     *
     */
    public Optional<List<UaapGame>> scrapeAllGamesAndResults() {
        //Step1
        Elements gameScheds = getGameSchedElements(document);

        //Step2
        List<UaapGame> games = new ArrayList<>();

        //Step3
        for (Element gameSched : gameScheds) {
            //Step3.1
            UaapGame scrapedGame = getScrapeGame(gameSched);
            //Ste3.2
            scrapedGame.setUaapSeasonId(uaapSeason.getSeasonNumber() + "-" + uaapSeason.getGameCode().getGameCode());
            scrapedGame.setUrl(uaapSeason.getUrl());

            //Step3.3
            mapGameResultsToGame(gameSched, scrapedGame);

            //Step3.4
            games.add(scrapedGame);
//            break;
        }

        HashMap<Integer, Integer> gameIdMap = extractGameNumGameIdMap();
        for (UaapGame game : games) {
            Integer gameNumWeb = gameIdMap.get(game.getGameNumber());
            String urlStats = game.getUrl().replace(":id",  String.valueOf(gameNumWeb));
            Document doc;
            try {
                doc = getDocumentStats(urlStats);
            } catch (IOException e) {
                continue;
            }
            addAdditionalGameData(doc, game);
        }

        if (!games.isEmpty()) {
            return Optional.of(games);
        }
        return Optional.empty();
    }
    /**
     * Step 3.1 in {@link #scrapeAllGamesAndResults()}
     *
     *  @param gameDoc
     * @return {@link Elements}
     *
     */
    protected abstract Elements getGameSchedElements(Document gameDoc);
    /**
     * Step 3.1 in {@link #scrapeAllGamesAndResults()}
     *
     * @return {@link Element}
     *
     */
    protected abstract UaapGame getScrapeGame(Element gameSched);
    /**
     * Step 3.1 in {@link #scrapeAllGamesAndResults()}
     *
     */
    protected abstract void mapGameResultsToGame(Element gameSched, UaapGame scrapeGame);


    public UaapGame updateAdditionalData(int gameNum) {
        List<UaapGame> uaapGameDtos = scrapeAllGamesAndResults().orElseThrow(
                () -> new RuntimeException(String.format("No game data found: %s", uaapSeason.getUrl()))
        );

        UaapGame uaapGameDto = uaapGameDtos.stream().filter(g -> g.getGameNumber() == gameNum).findFirst().orElseThrow(
                () -> new RuntimeException(String.format("No game data found of game number: %s", gameNum))
        );

        //map of UaapGameDb gameNumber and server gameNumber
        HashMap<Integer, Integer> gameIdMap = extractGameNumGameIdMap();
        Integer gameNumWeb = gameIdMap.get(gameNum);
        String urlStats = uaapSeason.getUrl().replace(":id",  String.valueOf(gameNumWeb));

        try {
            Document doc = getDocumentStats(urlStats);
            HashMap<String, List<PlayerStat>> playerStatsMap = new HashMap<>();
            addAdditionalGameData(doc, uaapGameDto);
            return uaapGameDto;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public HashMap<String, List<PlayerStat>> scrapeAllPlayerStatsfromGame(int gamuNum) {
        List<UaapGame> uaapGameDtos = scrapeAllGamesAndResults().orElseThrow(
                () -> new RuntimeException(String.format("No game data found: %s", uaapSeason.getUrl()))
        );

        UaapGame uaapGameDto = uaapGameDtos.stream().filter(g -> g.getGameNumber() == gamuNum).findFirst().orElseThrow(
                () -> new RuntimeException(String.format("No game data found of game number: %s", gamuNum))
        );

        //map of UaapGameDb gameNumber and server gameNumber
        HashMap<Integer, Integer> gameIdMap = extractGameNumGameIdMap();
        Integer gameNumWeb = gameIdMap.get(gamuNum);
        String urlStats = uaapSeason.getUrl().replace(":id",  String.valueOf(gameNumWeb));

        try {
            Document doc = getDocumentStats(urlStats);
            HashMap<String, List<PlayerStat>> playerStatsMap = new HashMap<>();
            for (GameResult gameResult : uaapGameDto.getGameResults()) {
                List<PlayerStat> playerStats = scrapePlayerStats(gameResult, doc);
                playerStatsMap.put(gameResult.getId(), playerStats);
            }
            return playerStatsMap;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }


    //    ------------------- SCRAPE STATS FOR EACH GAME ----------------------//
    //Scrape stats for each game
    public List<PlayerStat> scrapePlayerStats(GameResult gameResult, Document doc){
        //Player stats table BODY elements (HOME, AWAY) - should be size 2
        Elements playerStatTables = getTableBodyElements(doc);

        //--get table element if Home or Away (index0-> Home, index1-> Away)
        Element teamTableElement = "HOME".equals(gameResult.getTeamTag()) ? playerStatTables.get(0) : playerStatTables.get(1);

        //--get all row elements and convert to csv list
        List<String> playerStatCsvList = teamTableElement.select("tr")
                .stream()
                .map(tr ->                          //for each row convert each players data to csv
                        tr.select("td")
                                .stream()
                                .map(Element::text)
                                .map(str -> str.replace(",", ""))
                                .collect(Collectors.joining(","))
                )
                .toList();

        //extract player stats
        List<PlayerStat> playerStatList = getPlayerStatList(playerStatCsvList, gameResult, getPlayerStatsFactory());

        return playerStatList;
    }

    protected abstract List<PlayerStat> getPlayerStatList(List<String> playerStatCsvList, GameResult gameResult, PlayerStatsFactory playerStatsFactory);

    protected abstract Elements getTableBodyElements(Document gameDocument);

    protected abstract PlayerStatsFactory getPlayerStatsFactory();



    //------------------- Utility methods ----------------------//

    public abstract HashMap<Integer, Integer> extractGameNumGameIdMap();

    public abstract void addAdditionalGameData(Document doc, UaapGame game);

    //    for getting stats (will try to fetch 3 times with 1 second of waiting time)
    private Document getDocumentStats(String urlStats) throws IOException {
        int maxAttempt = 3;
        int i = 0;
        Document doc;
        while (true) {
            try {
                doc = UtilityClass.getGameDocument(urlStats);
                return doc;
            } catch (HttpStatusException e) {
                if (e.getStatusCode() >= 500) {
                    if (i < 3) {
                        ++i;
                        try {
                            Thread.sleep(1000 * 1);
                        } catch (InterruptedException ex) {
                            throw new RuntimeException(ex);
                        }
                    }else {
                        throw e;
                    }
                }
            }
        }
    }

    //------------------- SIMPLE FACTORY ----------------------////
    public static GameScraper gameScraperFactory(UaapSeason uaapSeason)  {
        String gameCode = uaapSeason.getGameCode().getGameCode();
        if (gameCode.endsWith("BB")) {
            try {
                return new BballGameScraper(uaapSeason);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else if (gameCode.endsWith("VB")) {
            try {
                return new VballGameScraper(uaapSeason);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        throw new RuntimeException("Game Code error: " + gameCode);
    }

}
