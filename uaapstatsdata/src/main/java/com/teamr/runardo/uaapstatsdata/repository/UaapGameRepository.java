package com.teamr.runardo.uaapstatsdata.repository;

import com.teamr.runardo.uaapstatsdata.entity.UaapGame;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UaapGameRepository extends JpaRepository<UaapGame, String> {
}
