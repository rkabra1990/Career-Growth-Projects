package com.example.intraday.repo;

import com.example.intraday.entities.PositionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PositionRepository extends JpaRepository<PositionEntity, Long> {
    Optional<PositionEntity> findBySymbol(String symbol);
}
