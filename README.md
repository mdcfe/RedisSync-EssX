# Redis Sync for EssentialsX

This plugin provides an [ISyncProvider] that syncs EssentialsX data through a
Redis-backed queue. This allows some data to be consistently stored across multiple
servers running this plugin with EssentialsX.

It also serves as the current model implementation of ISyncProvider.

See EssentialsX/Essentials#2020 for progress on support in EssentialsX.
