package skytales.Carts.util.redis.util;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import skytales.Carts.util.redis.RedisService;
import skytales.Carts.util.redis.sync.CacheStartupSync;
import skytales.Carts.util.state_engine.UpdateProducer;


@EnableAsync
@Getter
@Setter
@Component
@EnableScheduling
public class RedisHealthChecker {


    private final RedisTemplate<String, Object> redisTemplate;


    private final RedisService redisService;
    private final CacheStartupSync cacheStartupSync;
    private UpdateProducer updateProducer;

    private volatile boolean redisAvailable = false;

    public RedisHealthChecker(RedisTemplate<String, Object> redisTemplate, RedisService redisService, CacheStartupSync cacheStartupSync) {

        this.redisTemplate = redisTemplate;
        this.redisService = redisService;
        this.cacheStartupSync = cacheStartupSync;
    }

    @Async
    @Scheduled(fixedRate = 120000)
    public void checkRedisHealth() {

        try {
            if (!redisAvailable) {
                redisAvailable = true;
                updateProducer.sendRedisSyncRequest();
                return;
            }

            redisService.checkAndCleanMemory();
            updateProducer.sendBatchSyncRequest();
        } catch (Exception e) {

            if (redisAvailable) {
                redisAvailable = false;
            }

        }
    }


}
