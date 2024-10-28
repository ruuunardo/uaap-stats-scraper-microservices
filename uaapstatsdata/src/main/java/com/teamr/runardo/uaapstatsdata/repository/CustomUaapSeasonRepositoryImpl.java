package com.teamr.runardo.uaapstatsdata.repository;

import com.teamr.runardo.uaapstatsdata.entity.UaapSeason;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

import java.util.Objects;

public class CustomUaapSeasonRepositoryImpl implements CustomUaapSeasonRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @Transactional
    public UaapSeason customSaveGame(UaapSeason uaapSeason) {
        return entityManager.merge(uaapSeason);
    }

    @PostConstruct
    public void postConstruct() {
        Objects.requireNonNull(entityManager);
    }
}
