package covy.stockkeepingwithredis.service;

import covy.stockkeepingwithredis.entity.Stock;
import covy.stockkeepingwithredis.repository.StockRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class StockServiceTest {
    @Autowired
    private StockService stockService;

    @Autowired
    private StockRepository stockRepository;

    // 각 테스트가 실행되기 전에 데이터베이스에 테스트 데이터 생성
    @BeforeEach
    public void before() {
        stockRepository.saveAndFlush(new Stock(1L, 100L));
    }

    // 각 테스트를 실행한 후에 데이터베이스에 테스트 데이터 삭제
    @AfterEach
    public void after() {
        stockRepository.deleteAll();
    }

    @Test
    public void 재고감소() {
        // when
        stockService.decrease(1L, 1L);

        // then : 1L 상품의 재고 : 100 - 1 = 99개가 남아있어야함
        Stock stock = stockRepository.findById(1L).orElseThrow();
        assertEquals(99, stock.getQuantity());
    }
}