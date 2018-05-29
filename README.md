# Redis Sync for EssentialsX

[ISyncProvider]: https://github.com/N3FS/Essentials/blob/sync-framework/Essentials/src/net/ess3/api/sync/ISyncProvider.java "interface net.ess3.api.sync.ISyncProvider (in PR branch)"
[pr]: https://github.com/EssentialsX/Essentials/pull/2020 "PR #2020: Add a basic sync framework"

This plugin provides an [ISyncProvider][ISyncProvider] that syncs EssentialsX data through a
Redis-backed queue. This allows some data to be consistently stored across multiple
servers running this plugin with EssentialsX.

It also serves as the current model implementation of ISyncProvider.

See the [relevant EssentialsX PR][pr] for progress on support in EssentialsX.

## Currently supported data

| Data                | Format                           |
|---------------------|----------------------------------|
| Receiving mail      | `MAIL to <uuid> reads <message>` |
| Clearing mail       | `MAIL of <uuid> clear`           |
| Mute/Unmute         | `MUTE of <uuid> is <true/false>` |
| Mute timeout        | `MUTE of <uuid> timeout <time>`  |
| Nickname change     | `NICK of <uuid> now <nickname>`  |