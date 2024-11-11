package com.teamr.runardo.uaapstatscraper.scraper;

import com.teamr.runardo.uaapstatscraper.dto.GameResultDto;
import com.teamr.runardo.uaapstatscraper.dto.UaapGameDto;
import com.teamr.runardo.uaapstatscraper.dto.UaapSeasonDto;
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
    protected UaapSeasonDto uaapSeasonDto;

    /** Set document(using the URL)
     *
     * @param uaapSeasonDto season information of UAAP
     * @throws RuntimeException if can't connect to the server
     */
    protected GameScraper(UaapSeasonDto uaapSeasonDto) throws IOException {
        this.uaapSeasonDto = uaapSeasonDto;
        this.document = UtilityClass.getGameDocument(uaapSeasonDto.getUrl());
    }

    protected GameScraper(){
    }

    /**
     * <h6>Scrapes all Uaap Games</h2>
     * <ol>
     *  <li>Get game schedule elements from Document for list of UAAP Games extraction</li>
     *  <li>Create {@code List} of {@link UaapGameDto}</li>
     *  <li>Iterate each element to extract each game data</li>
     *      <ol>
     *          <li>Get {@link UaapGameDto} game number</li>
     *          <li>Set {@code uaapSeasonId}({@link UaapSeasonDto} {@code seasonNumber}-{@code gameCode})</li>
     *          <li>Extract {@link GameResultDto}({@code home} and {@code away} teams) and map to each games</li>
     *          <li>Add to created {@code List} of {@link UaapGameDto} </li>
     *      </ol>
     *  <li>Map additional data(venue and schedule) to each game</li>
     *
     * </ol>
     * @return {@code List} of {@link UaapGameDto}
     *
     */
    public Optional<List<UaapGameDto>> scrapeAllGamesAndResults() {
        //Step1
        Elements gameScheds = getGameSchedElements(document);

        //Step2
        List<UaapGameDto> games = new ArrayList<>();

        //Step3
        for (Element gameSched : gameScheds) {
            //Step3.1
            UaapGameDto scrapedGame = getScrapeGame(gameSched);
            //Ste3.2
            scrapedGame.setUaapSeasonId(uaapSeasonDto.getSeasonNumber() + "-" + uaapSeasonDto.getGameCode().getGameCode());
            scrapedGame.setUrl(uaapSeasonDto.getUrl());

            //Step3.3
            mapGameResultsToGame(gameSched, scrapedGame);

            //Step3.4
            games.add(scrapedGame);
//            break;
        }

//        HashMap<Integer, Integer> gameIdMap = extractGameNumGameIdMap();
//        for (UaapGameDto game : games) {
//            Integer gameNumWeb = gameIdMap.get(game.getGameNumber());
//            String urlStats = game.getUrl().replace(":id",  String.valueOf(gameNumWeb));
//            Document doc;
//            try {
//                doc = getDocumentStats(urlStats);
//            } catch (IOException e) {
//                continue;
//            }
//            addAdditionalGameData(doc, game);
//        }

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
    protected abstract UaapGameDto getScrapeGame(Element gameSched);
    /**
     * Step 3.1 in {@link #scrapeAllGamesAndResults()}
     *
     */
    protected abstract void mapGameResultsToGame(Element gameSched, UaapGameDto scrapeGame);


    public UaapGameDto updateAdditionalData(int gameNum) {
        List<UaapGameDto> uaapGameDtoDtos = scrapeAllGamesAndResults().orElseThrow(
                () -> new RuntimeException(String.format("No game data found: %s", uaapSeasonDto.getUrl()))
        );

        UaapGameDto uaapGameDto = uaapGameDtoDtos.stream().filter(g -> g.getGameNumber() == gameNum).findFirst().orElseThrow(
                () -> new RuntimeException(String.format("No game data found of game number: %s", gameNum))
        );

        //map of UaapGameDb gameNumber and server gameNumber
        HashMap<Integer, Integer> gameIdMap = extractGameNumGameIdMap();
        Integer gameNumWeb = gameIdMap.get(gameNum);
        String urlStats = uaapSeasonDto.getUrl().replace(":id",  String.valueOf(gameNumWeb));

        try {
            Document doc = getDocumentStats(urlStats);
            addAdditionalGameData(doc, uaapGameDto);
            return uaapGameDto;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public HashMap<String, List<PlayerStat>> scrapeAllPlayerStatsfromGame(int gamuNum) {
        List<UaapGameDto> uaapGameDtoDtos = scrapeAllGamesAndResults().orElseThrow(
                () -> new RuntimeException(String.format("No game data found: %s", uaapSeasonDto.getUrl()))
        );

        UaapGameDto uaapGameDto = uaapGameDtoDtos.stream().filter(g -> g.getGameNumber() == gamuNum).findFirst().orElseThrow(
                () -> new RuntimeException(String.format("No game data found of game number: %s", gamuNum))
        );

        //map of UaapGameDb gameNumber and server gameNumber
        HashMap<Integer, Integer> gameIdMap = extractGameNumGameIdMap();
        Integer gameNumWeb = gameIdMap.get(gamuNum);
        String urlStats = uaapSeasonDto.getUrl().replace(":id",  String.valueOf(gameNumWeb));

        try {
            Document doc = getDocumentStats(urlStats);
            HashMap<String, List<PlayerStat>> playerStatsMap = new HashMap<>();
            addAdditionalGameData(doc, uaapGameDto);
            for (GameResultDto gameResultDto : uaapGameDto.getGameResultDtos()) {
                List<PlayerStat> playerStats = scrapePlayerStats(gameResultDto, doc);
                playerStatsMap.put(gameResultDto.getId(), playerStats);
            }
            return playerStatsMap;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }


    //    ------------------- SCRAPE STATS FOR EACH GAME ----------------------//
    //Scrape stats for each game
    public List<PlayerStat> scrapePlayerStats(GameResultDto gameResultDto, Document doc){
        //Player stats table BODY elements (HOME, AWAY) - should be size 2
        Elements playerStatTables = getTableBodyElements(doc);

        //--get table element if Home or Away (index0-> Home, index1-> Away)
        Element teamTableElement = "HOME".equals(gameResultDto.getTeamTag()) ? playerStatTables.get(0) : playerStatTables.get(1);

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
        List<PlayerStat> playerStatList = getPlayerStatList(playerStatCsvList, gameResultDto, getPlayerStatsFactory());

        return playerStatList;
    }

    protected abstract List<PlayerStat> getPlayerStatList(List<String> playerStatCsvList, GameResultDto gameResultDto, PlayerStatsFactory playerStatsFactory);

    protected abstract Elements getTableBodyElements(Document gameDocument);

    protected abstract PlayerStatsFactory getPlayerStatsFactory();



    //------------------- Utility methods ----------------------//

    public abstract HashMap<Integer, Integer> extractGameNumGameIdMap();

    public abstract void addAdditionalGameData(Document doc, UaapGameDto game);

    //    for getting stats (will try to fetch 3 times with 1 second of waiting time)
    private Document getDocumentStats(String urlStats) throws IOException {
        int maxAttempt = 5;
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
                            Thread.sleep(2000 * 1);
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
    public static GameScraper gameScraperFactory(UaapSeasonDto uaapSeasonDto)  {
        String gameCode = uaapSeasonDto.getGameCode().getGameCode();
        if (gameCode.endsWith("BB")) {
            try {
                return new BballGameScraper(uaapSeasonDto);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else if (gameCode.endsWith("VB")) {
            try {
                return new VballGameScraper(uaapSeasonDto);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        throw new RuntimeException("Game Code error: " + gameCode);
    }

}
