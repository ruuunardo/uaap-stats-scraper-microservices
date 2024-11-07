package com.teamr.runardo.uaapstatsdata.repository;

import com.teamr.runardo.uaapstatsdata.entity.BballPlayerStat;
import com.teamr.runardo.uaapstatsdata.entity.CompositeStatId;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BBallPlayerStatRepository extends JpaRepository<BballPlayerStat, CompositeStatId> {

    @Query("DELETE FROM BballPlayerStat s WHERE s.gameResultId LIKE :gameId%")
    @Modifying
    @Transactional
    void deleteAllByGameId(@Param("gameId") String gameId);

    @Query("SELECT s FROM BballPlayerStat s WHERE s.gameResultId LIKE :gameId%")
    List<BballPlayerStat> findAllByGameId(String gameId);
}
