package covy.stockkeepingwithredis.facade;

import java.util.concurrent.TimeUnit;

import covy.stockkeepingwithredis.service.StockService;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class RedissonLockStockFacade {
    // Lock 획득/해제를 위한 RedissonClient 추가
    private final RedissonClient redissonClient;
    private final StockService stockService;

    public RedissonLockStockFacade(RedissonClient redissonClient, StockService stockService) {
        this.redissonClient = redissonClient;
        this.stockService = stockService;
    }

    public void decrease(Long id, Long quantity) {
        // RedissonClient을 활용하여 Lock 객체 조회
        RLock lock = redissonClient.getLock(id.toString());

        try {
            // Lock 획득 시도
            boolean available = lock.tryLock(
                    10, 	// lock 획득을 위한 대기 시간
                    5, 		// lock 점유 시간
                    TimeUnit.SECONDS
            );

            // Lock 획득에 실패한 경우
            if(!available) {
                log.info("lock 획득 실패");
                return;
            }

            // Lock 획득에 성공한 경우 - 비즈니스 로직 수행
            stockService.decrease(id, quantity);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            // 로직이 모두 정상적으로 수행되었다면 Lock을 해제한다.
            lock.unlock();
        }
    }
}
