package io.github.md678685.mcsync.essx;

import com.earth2me.essentials.User;

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
    public void addMail(User userData, String msg) {
        SyncPlugin.getInstance().getLogger().info("Sending outbound mail from " + userData.getLastAccountName());
        queue.publishMessage("MAIL to " + userData.getConfigUUID() + " reads " + msg);
    }

    @Override
    public void setNickname(User userData, String nick) {
        SyncPlugin.getInstance().getLogger().info("Sending nickname change for " + userData.getLastAccountName());
        queue.publishMessage("NICK of " + userData.getConfigUUID() + " now " + nick);
    }

	@Override
	public void clearMail(User arg0, String arg1) {
		
	}
}
