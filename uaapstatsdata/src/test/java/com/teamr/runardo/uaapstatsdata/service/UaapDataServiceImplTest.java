package com.teamr.runardo.uaapstatsdata.service;

import com.teamr.runardo.uaapstatsdata.dto.UaapGameDto;
import com.teamr.runardo.uaapstatsdata.dto.UaapSeasonDto;
import com.teamr.runardo.uaapstatsdata.entity.*;
import com.teamr.runardo.uaapstatsdata.exception.ResourceNotFoundException;
import com.teamr.runardo.uaapstatsdata.mapper.UaapGameMapper;
import com.teamr.runardo.uaapstatsdata.mapper.UaapSeasonMapper;
import com.teamr.runardo.uaapstatsdata.repository.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestPropertySource("classpath:application.yml")
@SpringBootTest
@Transactional
@Sql(scripts = "/data.sql")
class UaapDataServiceImplTest {
    @Autowired
    UaapDataService uaapDataService;

    @Autowired
    UaapSeasonRepository uaapSeasonRepository;

    @Autowired
    UaapGameRepository uaapGameRepository;

    @Autowired
    BBallPlayerStatRepository bBallPlayerStatRepository;

    @Autowired
    VBallPlayerStatRepository vBallPlayerStatRepository;

    @Autowired
    PlayerRepository playerRepository;

    @Autowired
    EntityManager entityManager;

    @Test
    public void testPreloadedUaapUniv() {
        TypedQuery<UaapUniv> query = entityManager.createQuery("select a from UaapUniv a", UaapUniv.class);
        List<UaapUniv> resultList = query.getResultList();

        assertEquals(8, resultList.size());
    }

    @Test
    public void createUaapSeason() {

        List<UaapSeason> allUaapSeason = uaapDataService.findAllUaapSeason();
        assertEquals(0, allUaapSeason.size());

        UaapSeasonDto uaapSeasonDto = new UaapSeasonDto();
        uaapSeasonDto.setSeasonNumber(1);
        uaapSeasonDto.setUrl("testurl.com");
        uaapSeasonDto.setGameCode(new UaapGameCode("MBB", "Men's Basketball"));

        uaapDataService.saveUaapSeason(uaapSeasonDto);
        allUaapSeason = uaapDataService.findAllUaapSeason();
        assertEquals(1, allUaapSeason.size());
    }

    @Test
    public void findUaapSeasonById() {
        UaapSeasonDto uaapSeasonDto = new UaapSeasonDto();
        uaapSeasonDto.setSeasonNumber(1);
        uaapSeasonDto.setUrl("testurl.com");
        uaapSeasonDto.setGameCode(new UaapGameCode("MBB", "Men's Basketball"));
        uaapDataService.saveUaapSeason(uaapSeasonDto);

        String id = String.join("-", String.valueOf(uaapSeasonDto.getSeasonNumber()), uaapSeasonDto.getGameCode().getGameCode());
        UaapSeason uaapSeason = uaapDataService.findUaapSeasonById(id);
        assertNotNull(uaapSeason);

        assertThrows(ResourceNotFoundException.class, () -> {
            uaapDataService.findUaapSeasonById("test");
        });
    }

    @Test
    @Sql(scripts = {"classpath:/data.sql", "classpath:/insertUaapSeason.sql", "classpath:/insertUaapGame.sql", "classpath:/insertStats.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void deleteUaapSeasonById() {
        TypedQuery<UaapUniv> query = entityManager.createQuery("select a from UaapUniv a", UaapUniv.class);
        List<UaapUniv> resultList = query.getResultList();

        assertEquals(8, resultList.size());
        String id = "1-MBB";
        UaapSeason uaapSeason = uaapDataService.findUaapSeasonById(id);
        assertNotNull(uaapSeason);

        assertTrue(uaapDataService.deleteUaapSeasonById(id));
        assertThrows(ResourceNotFoundException.class, () -> {
            uaapDataService.findUaapSeasonById(id);
        });

        List<? extends PlayerStat> stats = uaapDataService.findUaapStatsByGameId(id);
        assertEquals(0, stats.size());
    }

    @Test
    public void createUaapGame() {
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
        assertEquals("1-MBB-1", game.getId());
    }

    @Test
    public void createUaapGameNoUaapSeason() {
        UaapGameDto uaapGameDto = new UaapGameDto();
        uaapGameDto.setUaapSeasonId("1-MBB");
        uaapGameDto.setGameNumber(1);
        uaapGameDto.setUrl("testurl.com");

        //throws resource exception
        assertThrows(ResourceNotFoundException.class, () -> {
            uaapDataService.saveUaapGame(uaapGameDto);
        });

        //game not saved
        assertTrue(uaapGameRepository.findById("1-MBB-1").isEmpty());
    }

    @Test
    public void findUaapGameById() {
        UaapSeasonDto uaapSeasonDto = new UaapSeasonDto();
        uaapSeasonDto.setSeasonNumber(1);
        uaapSeasonDto.setUrl("testurl.com");
        uaapSeasonDto.setGameCode(new UaapGameCode("MBB", "Men's Basketball"));

        uaapDataService.saveUaapSeason(uaapSeasonDto);

        UaapGameDto uaapGameDto = new UaapGameDto();
        uaapGameDto.setUaapSeasonId("1-MBB");
        uaapGameDto.setGameNumber(1);
        uaapGameDto.setUrl("testurl.com");

        uaapDataService.saveUaapGame(uaapGameDto);

        UaapGame game = uaapDataService.findUaapGameById("1-MBB-1");
        assertEquals("1-MBB-1", game.getId());
        assertEquals("1-MBB", game.getSeasonId());

        //Not Found UaapGame
        assertThrows(ResourceNotFoundException.class, () -> {
            uaapDataService.findUaapGameById("1-TEST-1");
        });
    }

    @Test
    public void deleteUaapGameById() {
        UaapSeasonDto uaapSeasonDto = new UaapSeasonDto();
        uaapSeasonDto.setSeasonNumber(1);
        uaapSeasonDto.setUrl("testurl.com");
        uaapSeasonDto.setGameCode(new UaapGameCode("MBB", "Men's Basketball"));

        UaapGameDto uaapGameDto = new UaapGameDto();
        uaapGameDto.setUaapSeasonId("1-MBB");
        uaapGameDto.setGameNumber(1);
        uaapGameDto.setUrl("testurl.com");

        UaapSeason uaapSeason = UaapSeasonMapper.mapToUaapSeason(uaapSeasonDto, new UaapSeason());

        UaapSeason savedUaapSeason = uaapSeasonRepository.save(uaapSeason);
        UaapGame savedUaapGame = uaapGameRepository.save(UaapGameMapper.mapToUaapGame(uaapGameDto, new UaapGame()));

        assertEquals("1-MBB", savedUaapSeason.getId());
        assertEquals("1-MBB-1", savedUaapGame.getId());

        assertTrue(uaapDataService.deleteUaapGameById("1-MBB-1"));
        assertThrows(ResourceNotFoundException.class, () -> {
            uaapDataService.deleteUaapGameById("1-MBB-2");
        });
        assertTrue(uaapGameRepository.findById("1-MBB-1").isEmpty());
    }

    @Test
    public void saveUaapStatsBball() {
        assertTrue(bBallPlayerStatRepository.findAll().isEmpty());
        assertTrue(playerRepository.findAll().isEmpty());

        BballPlayerStat bballPlayerStat = new BballPlayerStat();
        Player player = new Player();
        player.setId("P1");
        player.setName("Player 1");
        bballPlayerStat.setPlayer(player);
        bballPlayerStat.setGameResultId("1-MBB-1-UP");
        bballPlayerStat.setPoints(20);

        uaapDataService.saveUaapStats(bballPlayerStat);
        assertFalse(bBallPlayerStatRepository.findAll().isEmpty());
        assertFalse(playerRepository.findAll().isEmpty());
    }

    @Test
    public void saveUaapStatsVball() {
        assertTrue(vBallPlayerStatRepository.findAll().isEmpty());
        assertTrue(playerRepository.findAll().isEmpty());

        VballPlayerStat playerStat = new VballPlayerStat();
        Player player = new Player();
        player.setId("P1");
        player.setName("Player 1");
        playerStat.setPlayer(player);
        playerStat.setGameResultId("1-MVB-1-UP");
        playerStat.setAttackAttempt(20);

        uaapDataService.saveUaapStats(playerStat);
        assertFalse(vBallPlayerStatRepository.findAll().isEmpty());
        assertFalse(playerRepository.findAll().isEmpty());
    }

    @Test
    public void saveUaapStatsList() {
        assertTrue(vBallPlayerStatRepository.findAll().isEmpty());
        assertTrue(bBallPlayerStatRepository.findAll().isEmpty());
        assertTrue(playerRepository.findAll().isEmpty());

        VballPlayerStat playerStat = new VballPlayerStat();
        Player player = new Player();
        player.setId("P1");
        player.setName("Player 1");
        playerStat.setPlayer(player);
        playerStat.setGameResultId("1-MVB-1-UP");
        playerStat.setAttackAttempt(20);

        BballPlayerStat bballPlayerStat = new BballPlayerStat();
//        Player player = new Player();
        player.setId("P1");
        player.setName("Player 1");
        bballPlayerStat.setPlayer(player);
        bballPlayerStat.setGameResultId("1-MBB-1-UP");
        bballPlayerStat.setPoints(20);

        uaapDataService.saveUaapStats(Arrays.asList(playerStat, bballPlayerStat));
        assertFalse(vBallPlayerStatRepository.findAll().isEmpty());
        assertFalse(bBallPlayerStatRepository.findAll().isEmpty());
        assertFalse(playerRepository.findAll().isEmpty());
    }

    @Test
    @Sql("/insertStats.sql")
    public void deleteUaapStatsByGameId() {
        assertEquals(2, bBallPlayerStatRepository.findAll().size());

        List<? extends PlayerStat> stats = uaapDataService.findUaapStatsByGameId("1-MBB-1");
        assertEquals(2, stats.size());

        uaapDataService.deleteUaapStatsByGameId("1-MBB-1");
        stats = uaapDataService.findUaapStatsByGameId("1-MBB-1");
        assertEquals(0, stats.size());
    }

    @Test
    @Sql("/insertStats.sql")
    public void findStatsByGameId() {
        assertDoesNotThrow(() -> {
            uaapDataService.findUaapStatsByGameId("1-MBB-1");});

        assertThrows(RuntimeException.class,() -> {
            uaapDataService.findUaapStatsByGameId("1-XXX-1");});
    }

}







