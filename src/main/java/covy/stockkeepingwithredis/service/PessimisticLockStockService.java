package covy.stockkeepingwithredis.service;

import covy.stockkeepingwithredis.entity.Stock;
import covy.stockkeepingwithredis.repository.StockRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * PessimisticLockStockService - 비관적 락(조회 시점에 테이블에 락을 건다)을 사용
 *
 * @author : Covy
 * @date : 2025-01-21
 */

@Service
public class PessimisticLockStockService {
    private final StockRepository stockRepository;

    public PessimisticLockStockService(StockRepository stockRepository) {
        this.stockRepository = stockRepository;
    }

    @Transactional
    public void decrease(Long id, Long quantity) {
        // 락을 걸고 재고 데이터를 가져온다.
        Stock stock = stockRepository.findByIdWithPessimisticLock(id);

        stock.decrease(id);
        stockRepository.save(stock);
    }
}
