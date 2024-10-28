package com.teamr.runardo.uaapstatsdata.repository;


import com.teamr.runardo.uaapstatsdata.entity.UaapSeason;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UaapSeasonRepository extends JpaRepository<UaapSeason, Integer>, CustomUaapSeasonRepository {
    @Query(
            value = "select s from UaapSeason s where s.seasonNumber=:seasonNum and s.gameCode.gameCode=:gameCode"
    )
    Optional<UaapSeason> findAllBySeasonNumberAndGameCode(@Param("seasonNum") int gameNum, @Param("gameCode") String gameCode);
}
