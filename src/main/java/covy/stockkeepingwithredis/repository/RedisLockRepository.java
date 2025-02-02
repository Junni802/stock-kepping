package covy.stockkeepingwithredis.repository;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;

import static org.springframework.cache.interceptor.SimpleKeyGenerator.generateKey;

@Component
public class RedisLockRepository {
    //레디스를 사용하기 위해 RedisTemplate 클래스를 변수로 추가한다
    private RedisTemplate<String, String> redisTemplate;

    public RedisLockRepository(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    // Lock메서드를 구현한다
    public Boolean lock(Long key) {
        return redisTemplate
                .opsForValue()
                // setnx 명령어 호출
                .setIfAbsent(
                        generateKey(key), // key
                        "lock", // value
                        Duration.ofMillis(3_000) // lock timeout 지정(e.g. 3000ms(3sec))
                );
    }

    // Lock을 해제하기 위한 메서드
    public Boolean unlock(Long key) {
        return redisTemplate.delete(generateKey(key));
    }


    private String generateKey(Long key) {
        return key.toString();
    }
}
