package com.agenticai.stock.repository;

import com.agenticai.stock.model.InvestmentPortfolio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PortfolioRepository extends JpaRepository<InvestmentPortfolio, Long> {
}
