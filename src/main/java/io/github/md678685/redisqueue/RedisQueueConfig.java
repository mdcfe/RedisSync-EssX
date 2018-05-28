package io.github.md678685.redisqueue;

public interface RedisQueueConfig {

    String getAddress();

    int getPort();

    String getPrefix();

    String getConsumerId();

    String getMessagesListKey();

    String getConsumerSetKey();

    String getMessagesListKeyFor(String consumer);
}
