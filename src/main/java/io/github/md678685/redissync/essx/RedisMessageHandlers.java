package io.github.md678685.redissync.essx;

import io.github.md678685.redisqueue.RedisQueue;
import me.lucko.helper.Schedulers;
import net.ess3.api.IEssentials;

import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RedisMessageHandlers {

    private static final Pattern MAIL_PATTERN = Pattern.compile("MAIL to ([0-9a-f-]+) reads (.+)");
    private static final Pattern NICK_PATTERN = Pattern.compile("NICK of ([0-9a-f-]+) now (.+)");

    public static void registerHandlers(RedisQueue queue, IEssentials ess) {
        queue.addHandler(msg -> {
            RedisSyncPlugin.getInstance().getLogger().info("Inbound: " + msg);
            return false;
        });

        queue.addHandler(msg -> {
            Matcher m = MAIL_PATTERN.matcher(msg);
            if (m.find()) {
                UUID uuid = UUID.fromString(m.group(1));
                String mailMsg = m.group(2);

                RedisSyncPlugin.getInstance().getLogger().info(uuid + " received mail " + mailMsg);

                ess.getUser(uuid).addMail(mailMsg, false);
                return true;
            }
            return false;
        });

        queue.addHandler(msg -> {
            Matcher m = NICK_PATTERN.matcher(msg);
            if (m.find()) {
                UUID uuid = UUID.fromString(m.group(1));
                String nick = m.group(2);

                RedisSyncPlugin.getInstance().getLogger().info(uuid + "'s nick changed to " + nick);

                ess.getUser(uuid).setNickname(nick, false);
                return true;
            }
            return false;
        });
    }

    public static void startAwaitTask(RedisQueue queue) {
        Schedulers.async().runRepeating(queue::awaitMessage, 0, 100);
    }

}
