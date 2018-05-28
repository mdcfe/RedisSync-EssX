package io.github.md678685.redisqueue;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

public class RedisQueue {

    private JedisPool pool;
    private RedisQueueConfig config;

    private List<Function<String, Boolean>> handlers = new ArrayList<>();

    public RedisQueue(RedisQueueConfig config) {
        this.pool = new JedisPool(config.getAddress(), config.getPort());;
        this.config = config;
    }

    private void withJedis(Consumer<Jedis> c) {
        try (Jedis jedis = pool.getResource()) {
            c.accept(jedis);
        }
    }

    /**
     * Register a consumer in the consumers set, making other servers aware of it.
     *
     */
    public void registerConsumer() {
        withJedis(jedis -> {
            jedis.sadd(config.getConsumerSetKey(), config.getConsumerId());
        });
    }

    /**
     * Publish a message to all registered consumers.
     *
     * @param msg Message to publish to consumers.
     */
    public void publishMessage(String msg) {
        withJedis(jedis -> {
            // Get all consumers
            jedis.smembers(config.getConsumerSetKey()).stream()
                    .filter(consumer -> !consumer.equals(config.getConsumerId()))
                    // Push message to each consumer's message list
                    .forEach(consumer -> jedis.rpush(config.getMessagesListKeyFor(consumer), msg));
        });
    }

    public void awaitMessage() {
        withJedis(jedis -> {
            List<String> response = jedis.blpop(5, config.getMessagesListKey());
            if (response.size() != 2) return;

            String msg = response.get(1);

            handleMessage(msg);
        });
    }

    public void addHandler(Function<String, Boolean> handler) {
        handlers.add(handler);
    }

    private void handleMessage(String msg) {
        for (Function<String, Boolean> handler : handlers) {
            if (handler.apply(msg)) return;
        }
    }

    public void close() {
        pool.close();
    }
}
