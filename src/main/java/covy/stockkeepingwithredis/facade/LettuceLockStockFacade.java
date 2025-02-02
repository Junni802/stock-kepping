package covy.stockkeepingwithredis.facade;

import covy.stockkeepingwithredis.repository.RedisLockRepository;
import covy.stockkeepingwithredis.service.StockService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class LettuceLockStockFacade {
    private final RedisLockRepository repository;
    private final StockService stockService;

    public LettuceLockStockFacade(RedisLockRepository repository, StockService stockService) {
        this.repository = repository;
        this.stockService = stockService;
    }

    public void decrease(Long id, Long quantity) throws InterruptedException {
        // Lock 획득
        while (!repository.lock(id)) {
            log.info("lock 획득 실패");
            Thread.sleep(100);
        }

        // Lock 획득에 성공했다면 재고 감소 로직 실행
        log.info("lock 획득");
        try {
            stockService.decrease(id, quantity);
        } finally {
            // 로직이 모두 수행되었다면 Lock 해제
            repository.unlock(id);
            log.info("lcok 해제");
        }
    }
}
