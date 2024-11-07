package com.teamr.runardo.uaapstatsdata.repository;

import com.teamr.runardo.uaapstatsdata.entity.CompositeStatId;
import com.teamr.runardo.uaapstatsdata.entity.VballPlayerStat;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface VBallPlayerStatRepository extends JpaRepository<VballPlayerStat, CompositeStatId> {

    @Query("DELETE FROM VballPlayerStat s WHERE s.gameResultId LIKE :gameId%")
    @Modifying
    @Transactional
    void deleteAllByGameId(@Param("gameId") String gameId);
}
