package io.github.md678685.redissync.essx;

import com.earth2me.essentials.UserData;
import com.earth2me.essentials.sync.ISyncProvider;
import io.github.md678685.redisqueue.RedisQueue;

/**
 * Sync framework provider
 */
public class RedisSyncProvider implements ISyncProvider {

    private RedisQueue queue;

    public RedisSyncProvider(Config config, RedisQueue queue) {
        this.queue = queue;
    }

    @Override
    public void addMail(UserData userData, String msg) {
        RedisSyncPlugin.getInstance().getLogger().info("Sending outbound mail from " + userData.getLastAccountName());
        queue.publishMessage("MAIL to " + userData.getConfigUUID() + " reads " + msg);
    }

    @Override
    public void setNickname(UserData userData, String nick) {
        RedisSyncPlugin.getInstance().getLogger().info("Sending nickname change for " + userData.getLastAccountName());
        queue.publishMessage("NICK of " + userData.getConfigUUID() + " now " + nick);
    }
}
