package com.teamr.runardo.uaapstatscraper.scraper;

import com.teamr.runardo.uaapstatscraper.utility.UtilityClass;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class VballGameScraperTest {

    @Test
    void getGameSchedElements() throws IOException {
        String url = "https://uaapvolleyball.livestats.ph/tournaments/uaap-volleyball-season-86-men-s?game_id=1";

        Document gameDocument = UtilityClass.getGameDocument(url);
        String cssQuery = "div.boxscorewrap table.box-score tbody:not(tbody:has(.team-totals))";
        Elements elements = gameDocument.select(cssQuery);
        System.out.println(elements.size());
    }
}