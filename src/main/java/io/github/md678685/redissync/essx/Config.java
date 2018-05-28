package io.github.md678685.redissync.essx;

import io.github.md678685.redisqueue.RedisQueueConfig;
import me.lucko.helper.config.ConfigurationNode;

import java.util.UUID;

public class Config implements RedisQueueConfig {

    private ConfigurationNode baseNode;
    private RedisSyncPlugin plugin;

    Config(RedisSyncPlugin plugin) {
        this.plugin = plugin;
        baseNode = plugin.loadConfigNode("config.yml");
    }

    public String getAddress() {
        return baseNode.getNode("redis", "address").getString("localhost");
    }

    public int getPort() {
        return baseNode.getNode("redis", "port").getInt(6379);
    }

    public String getPrefix() {
        return baseNode.getNode("redis", "prefix").getString("essx");
    }

    public String getConsumerId() {
        String id = baseNode.getNode("redis", "id").getString();

        if (id == null || id.equalsIgnoreCase("")) {
            id = UUID.randomUUID().toString();
            baseNode.getNode("redis", "id").setValue(id);

            plugin.saveConfigNode("config.yml", baseNode);
        }

        return id;
    }

    @Override
    public String getMessagesListKey() {
        return String.format("%s.%s.messages", getPrefix(), getConsumerId());
    }

    @Override
    public String getConsumerSetKey() {
        return String.format("%s.consumers", getPrefix());
    }

    @Override
    public String getMessagesListKeyFor(String consumer) {
        return String.format("%s.%s.messages", getPrefix(), consumer);
    }
}
