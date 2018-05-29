package io.github.md678685.mcsync.essx;

import io.github.md678685.redisqueue.RedisQueue;
import net.ess3.api.IEssentials;

import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RedisMessageHandler {

    private static final Pattern MAIL_ADD_PATTERN = Pattern.compile("MAIL to ([0-9a-f-]+) reads (.+)");
    private static final Pattern MAIL_CLEAR_PATTERN = Pattern.compile("MAIL of ([0-9a-f-]+) clear");
    private static final Pattern MUTE_STATUS_PATTERN = Pattern.compile("MUTE of ([0-9a-f-]+) is (true|false)");
    private static final Pattern MUTE_TIMEOUT_PATTERN = Pattern.compile("MUTE of ([0-9a-f-]+) timeout ([0-9]+)");
    private static final Pattern NICK_PATTERN = Pattern.compile("NICK of ([0-9a-f-]+) now (.+)");

    private final IEssentials ess;

    RedisMessageHandler(IEssentials ess) {
        this.ess = ess;
    }

    void registerHandlers(RedisQueue queue, IEssentials ess) {
        queue.addHandler(this::onMessageReceived);

        queue.addHandler(this::onMailAdd);
        queue.addHandler(this::onMailClear);

        queue.addHandler(this::onMuteStatus);
        queue.addHandler(this::onMuteTimeout);

        queue.addHandler(this::onNickChange);
    }

    private boolean onMessageReceived(String msg) {
        SyncPlugin.getInstance().getLogger().info("Inbound: " + msg);
        return false;
    }

    private boolean onMailAdd(String msg) {
        Matcher m = MAIL_ADD_PATTERN.matcher(msg);
        if (m.find()) {
            UUID uuid = UUID.fromString(m.group(1));
            String mailMsg = m.group(2);

            SyncPlugin.getInstance().getLogger().info(uuid + " received mail " + mailMsg);

            ess.getUser(uuid).addMail(mailMsg, false);
            return true;
        }
        return false;
    }

    private boolean onMailClear(String msg) {
        Matcher m = MAIL_CLEAR_PATTERN.matcher(msg);
        if (m.find()) {
            UUID uuid = UUID.fromString(m.group(1));

            SyncPlugin.getInstance().getLogger().info(uuid + " mailbox emptied");

            ess.getUser(uuid).clearMail(false);
            return true;
        }
        return false;
    }

    private boolean onMuteStatus(String msg) {
        Matcher m = MUTE_STATUS_PATTERN.matcher(msg);
        if (m.find()) {
            UUID uuid = UUID.fromString(m.group(1));
            String muteStatus = m.group(2);

            SyncPlugin.getInstance().getLogger().info(uuid + " mute status set to " + muteStatus);

            ess.getUser(uuid).setMuted(Boolean.parseBoolean(muteStatus), false);
            return true;
        }
        return false;
    }

    private boolean onMuteTimeout(String msg) {
        Matcher m = MUTE_TIMEOUT_PATTERN.matcher(msg);
        if (m.find()) {
            UUID uuid = UUID.fromString(m.group(1));
            String muteTimeout = m.group(2);

            SyncPlugin.getInstance().getLogger().info(uuid + " mute timeout set to " + muteTimeout);

            ess.getUser(uuid).setMuteTimeout(Long.parseLong(muteTimeout));
            return true;
        }
        return false;
    }

    private boolean onNickChange(String msg) {
        Matcher m = NICK_PATTERN.matcher(msg);
        if (m.find()) {
            UUID uuid = UUID.fromString(m.group(1));
            String nick = m.group(2);

            SyncPlugin.getInstance().getLogger().info(uuid + "'s nick changed to " + nick);

            ess.getUser(uuid).setNickname(nick, false);
            return true;
        }
        return false;
    }

}
