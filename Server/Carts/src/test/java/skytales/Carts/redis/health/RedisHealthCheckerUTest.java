//package skytales.Carts.redis.health;
//
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.data.redis.core.RedisTemplate;
//import skytales.Carts.util.redis.RedisService;
//import skytales.Carts.util.redis.util.RedisHealthChecker;
//import skytales.Carts.util.state_engine.UpdateProducer;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.*;
//
//@ExtendWith(MockitoExtension.class)
//class RedisHealthCheckerUTest {
//
//    @Mock
//    private UpdateProducer updateProducer;
//
//    @Mock
//    private RedisService redisService;
//
//    @Mock
//    private RedisTemplate<String, Object> redisTemplate;
//
//    @InjectMocks
//    private RedisHealthChecker redisHealthChecker;
//
//    @BeforeEach
//    void setUp() {
//        redisHealthChecker = new RedisHealthChecker(redisTemplate, redisService, updateProducer);
//    }
//
//    @Test
//    void checkRedisHealth_RedisAvailable() throws InterruptedException {
//        redisHealthChecker.setRedisAvailable(true);
//
//        doNothing().when(updateProducer).sendBatchSyncRequest();
//
//        redisHealthChecker.checkRedisHealth();
//
//        verify(updateProducer, times(1)).sendBatchSyncRequest();
//        assertTrue(redisHealthChecker.isRedisAvailable());
//    }
//
//    @Test
//    void checkRedisHealth_RedisNotAvailable() throws InterruptedException {
//        redisHealthChecker.setRedisAvailable(false);
//
//        redisHealthChecker.checkRedisHealth();
//
//        verify(updateProducer, never()).sendBatchSyncRequest();
//    }
//
//    @Test
//    void checkRedisHealth_RedisDown() throws InterruptedException {
//        redisHealthChecker.setRedisAvailable(true);
//
//        doThrow(new RuntimeException("Redis is down")).when(updateProducer).sendBatchSyncRequest();
//
//        redisHealthChecker.checkRedisHealth();
//
//        verify(updateProducer, times(1)).sendBatchSyncRequest();
//        assertFalse(redisHealthChecker.isRedisAvailable());
//    }
//}
