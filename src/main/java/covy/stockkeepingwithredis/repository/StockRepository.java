package covy.stockkeepingwithredis.repository;


import covy.stockkeepingwithredis.entity.Stock;
import org.springframework.data.jpa.repository.JpaRepository;

// StockRepository : 재고(Stock)에 대한 데이터베이스와의 CRUD 기능 제공
public interface StockRepository extends JpaRepository<Stock, Long> {
}