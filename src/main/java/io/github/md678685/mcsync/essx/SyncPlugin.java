package io.github.md678685.mcsync.essx;

import com.earth2me.essentials.Essentials;

import org.bukkit.plugin.ServicePriority;

import io.github.md678685.redisqueue.RedisQueue;
import me.lucko.helper.Schedulers;
import me.lucko.helper.Services;
import me.lucko.helper.config.ConfigFactory;
import me.lucko.helper.config.ConfigurationNode;
import me.lucko.helper.maven.MavenLibrary;
import me.lucko.helper.plugin.ExtendedJavaPlugin;
import me.lucko.helper.plugin.ap.Plugin;
import me.lucko.helper.plugin.ap.PluginDependency;
import net.ess3.api.IEssentials;
import net.ess3.api.sync.ISyncProvider;

/**
 * Redis sync plugin demonstrating the EssentialsX sync framework.
 * 
 */
@Plugin(name = "RedisSync-EssX", version = "${full.version}", authors = { "MD678685" }, depends = {
        @PluginDependency("Essentials") })
@MavenLibrary(groupId = "org.apache.commons", artifactId = "commons-pool2", version = "2.4.2")
@MavenLibrary(groupId = "redis.clients", artifactId = "jedis", version = "2.9.0")
public class SyncPlugin extends ExtendedJavaPlugin {

    private static SyncPlugin instance;

    private Config config;
    private IEssentials ess;
    private RedisQueue queue;

    @Override
    public void load() {
        instance = this;
    }

    @Override
    public void enable() {
        config = new Config(this);

        ess = getPlugin("Essentials", Essentials.class);

        queue = new RedisQueue(config);
        queue.registerConsumer();

        new RedisMessageHandler(ess).registerHandlers(queue, ess);

        Schedulers.async().runRepeating(queue::awaitMessage, 0, 100);

        Services.provide(ISyncProvider.class, new SyncProvider(config, queue), this, ServicePriority.High);
    }

    @Override
    public void disable() {
        queue.close();
    }

    public void saveConfigNode(String file, ConfigurationNode node) {
        ConfigFactory.yaml().save(getBundledFile(file), node);
    }

    public static SyncPlugin getInstance() {
        return instance;
    }

}
