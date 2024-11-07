package com.teamr.runardo.uaapstatsdata.repository;

import com.teamr.runardo.uaapstatsdata.entity.Player;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PlayerRepository extends JpaRepository<Player, String> {
    @Query("DELETE FROM Player p WHERE p.id LIKE :seasonId%")
    @Modifying
    @Transactional
    void deleteAllBySeasonId(@Param("seasonId") String seasonId);

}
