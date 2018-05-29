package io.github.md678685.mcsync.essx;

import com.earth2me.essentials.UserData;
import net.ess3.api.sync.AbstractSyncProvider;
import io.github.md678685.redisqueue.RedisQueue;

/**
 * Sync framework provider
 */
public class SyncProvider extends AbstractSyncProvider {

    private RedisQueue queue;

    public SyncProvider(Config config, RedisQueue queue) {
        this.queue = queue;
    }

    @Override
    public void addMail(UserData user, String msg) {
        SyncPlugin.getInstance().getLogger().info("Sending mail add for " + user.getLastAccountName());
        queue.publishMessage("MAIL to " + user.getConfigUUID() + " reads " + msg);
    }

    @Override
    public void clearMail(UserData user) {
        SyncPlugin.getInstance().getLogger().info("Sending mail clear for " + user.getLastAccountName());
        queue.publishMessage("MAIL of " + user.getConfigUUID() + " clear");
    }

    @Override
    public void setNickname(UserData user, String nick) {
        SyncPlugin.getInstance().getLogger().info("Sending nickname change for " + user.getLastAccountName());
        queue.publishMessage("NICK of " + user.getConfigUUID() + " now " + nick);
    }

    @Override
    public void setMuted(UserData user, boolean state) {
        SyncPlugin.getInstance().getLogger().info("Sending mute change for " + user.getLastAccountName());
        queue.publishMessage("MUTE of " + user.getConfigUUID() + " is " + state);
    }

    @Override
    public void setMuteTimeout(UserData user, long time) {
        SyncPlugin.getInstance().getLogger().info("Sending mute change for " + user.getLastAccountName());
        queue.publishMessage("MUTE of " + user.getConfigUUID() + " timeout " + time);
    }
}
