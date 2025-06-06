package skytales.Carts.util.redis.config;

import io.lettuce.core.RedisClient;
import io.lettuce.core.api.StatefulRedisConnection;
import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

public class OnRedisAvailableCondition implements Condition {
    @Override
    public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
        try {
            RedisClient redisClient = RedisClient.create("redis://redis:6379");
            StatefulRedisConnection<String, String> connection = redisClient.connect();

            boolean isConnected = connection.isOpen();
            connection.close();

            return isConnected;
        } catch (Exception e) {
            return false;
        }
    }
}