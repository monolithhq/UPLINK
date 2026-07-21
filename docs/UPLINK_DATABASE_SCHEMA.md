UPLINK Database Schema

Purpose

Single source of truth for Room persistence. Defines every table, entity, key, index, and relationship in UplinkDatabase.

Referenced by: data/database/UplinkDatabase.kt, SignalDao.kt, and entities/*.kt. No table should exist in code that isn't defined here first.

---

Database Overview

Name: uplink_database
Version: 1 (Phase 1 — no migrations needed yet; first migration will start at version 2)

Tables (5, matching UPLINK_DATA_MODELS.md Storage Strategy):

1. signals
2. cache
3. history
4. debug_logs
5. preferences

---

Table 1 — signals

Purpose: Local mirror of Signal metadata, populated from YouTubio /catalog and /search responses. This is the persisted counterpart to the Signal domain model.

Entity: SignalEntity

@Entity(tableName = "signals")
data class SignalEntity(
    @PrimaryKey val id: String,
    val title: String,
    val channel: String?,
    val thumbnail: String?,
    val duration: Long?,
    val streamUrl: String?,
    val createdAt: Long
)

Column| Type| Nullable| Notes
id| TEXT| No| Primary key. Matches YouTubio signal id (e.g. "yt_dQw4w9WgXcQ")
title| TEXT| No| —
channel| TEXT| Yes| Denormalized channel name (sourceNode), not a foreign key — Phase 1 has no separate channels table
thumbnail| TEXT| Yes| Thumbnail URL
duration| INTEGER| Yes| Seconds
streamUrl| TEXT| Yes| Last resolved playback URL; may be stale/expired, see UPLINK_YOUTUBIO_API.md expiresAt handling — the streamUrl column is a cache convenience only, never trusted without re-resolving if a playback attempt fails
createdAt| INTEGER| No| Epoch millis this row was first written

Indexes:

@Entity(
    tableName = "signals",
    indices = [Index(value = ["createdAt"]), Index(value = ["channel"])]
)

- Index on createdAt: supports "most recently seen" ordering for Home fallback when offline.
- Index on channel: supports future "signals from this source node" queries.

Notes:
- description, uploadedAt, watchingCount, and status from the full Signal domain model are intentionally NOT persisted — they're either large (description), re-fetched fresh each time (watchingCount), or derived at runtime (status). Only fields needed to render a SignalCard offline are stored.
- SignalMapper.kt owns the SignalEntity <-> Signal conversion in both directions.

---

Table 2 — cache

Purpose: Tracks cache freshness for signal lists (catalog pages, search result pages) independent of the signal rows themselves. Backs CachedSignal / CacheStatus from UPLINK_DATA_MODELS.md.

Entity: CacheEntryEntity

@Entity(tableName = "cache")
data class CacheEntryEntity(
    @PrimaryKey val cacheKey: String,
    val signalIds: String,
    val cachedAt: Long,
    val expiresAt: Long
)

Column| Type| Nullable| Notes
cacheKey| TEXT| No| Primary key. e.g. "catalog:default" or "search:kotlin coroutines"
signalIds| TEXT| No| Comma-separated list of signals.id, in display order (Room has no native array column type in Phase 1 — no need for a TypeConverter for a simple CSV of IDs)
cachedAt| INTEGER| No| Epoch millis
expiresAt| INTEGER| No| Epoch millis; SignalRepository treats entries past this as CacheStatus.EXPIRED

Indexes:

@Entity(tableName = "cache", indices = [Index(value = ["expiresAt"])])

- Index on expiresAt: supports a cleanup query that purges expired entries on app start.

Relationship to signals table: soft reference only (via CSV of ids), not a Room @ForeignKey. A cache entry referencing a signal id that's since been deleted from signals is treated as a partial cache miss for that id — SignalRepository re-fetches individual missing signals rather than invalidating the whole cache entry.

---

Table 3 — history

Purpose: Local watch history. Backs "recently played" if/when that's surfaced in UI; also used to resume playback position.

Entity: HistoryEntity

@Entity(tableName = "history")
data class HistoryEntity(
    @PrimaryKey(autoGenerate = true) val historyId: Long = 0,
    val signalId: String,
    val watchedAt: Long,
    val positionSeconds: Long,
    val completed: Boolean
)

Column| Type| Nullable| Notes
historyId| INTEGER| No| Auto-generated primary key
signalId| TEXT| No| References signals.id (soft reference, see below)
watchedAt| INTEGER| No| Epoch millis of this playback event
positionSeconds| INTEGER| No| Last known playback position, for resume
completed| INTEGER (Boolean)| No| True if playback reached >=95% of duration

Indexes:

@Entity(
    tableName = "history",
    indices = [Index(value = ["signalId"]), Index(value = ["watchedAt"])]
)

- Index on signalId: fast lookup of "have I watched this, and where did I leave off"
- Index on watchedAt: supports a "recently watched" query ordered by time

Relationship to signals table: soft reference (no @ForeignKey with cascade). If a signal is purged from the signals table, its history rows remain — history is a record of what happened, not a live join. UI resolves signalId -> title/thumbnail by re-fetching if the signals row is gone, and falls back to "unknown signal" display if YouTubio also can't resolve it.

One row is written per playback session start, and that same row is updated (not re-inserted) as position advances, keyed by historyId held in the active PlaybackSession's calling code.

---

Table 4 — debug_logs

Purpose: Persisted DebugEvent rows for DebugConsoleScreen, matching UPLINK_DATA_MODELS.md Debug Models.

Entity: DebugLogEntity

@Entity(tableName = "debug_logs")
data class DebugLogEntity(
    @PrimaryKey(autoGenerate = true) val logId: Long = 0,
    val timestamp: Long,
    val category: String,
    val message: String
)

Column| Type| Nullable| Notes
logId| INTEGER| No| Auto-generated primary key
timestamp| INTEGER| No| Epoch millis
category| TEXT| No| Stored as the String name of DebugCategory (APP, NETWORK, CACHE, PLAYER, YOUBTIO, ERROR) — no Room enum TypeConverter needed, DebugLogger.kt does the enum<->String conversion
message| TEXT| No| Free-text log message

Indexes:

@Entity(
    tableName = "debug_logs",
    indices = [Index(value = ["timestamp"]), Index(value = ["category"])]
)

- Index on timestamp: chronological display order in DebugConsoleScreen
- Index on category: supports filtering by category in the debug console

Retention rule: DebugLogger enforces a rolling cap (last 500 rows) by deleting oldest rows past that count on each write batch. This is application logic, not a Room feature — noted here so DebugLogger.kt and this schema stay in agreement.

Note: category preserves the existing YOUBTIO spelling from UPLINK_DATA_MODELS.md's DebugCategory enum as-is, since this schema must match the enum names exactly for the String conversion to round-trip. If that enum is ever renamed, this table's existing string rows become stale labels, not broken data — old rows just display under the old label.

---

Table 5 — preferences

Purpose: Backs UplinkPreferences from UPLINK_DATA_MODELS.md. Single-row table (Phase 1 has no multi-profile support).

Entity: PreferencesEntity

@Entity(tableName = "preferences")
data class PreferencesEntity(
    @PrimaryKey val id: Int = 0,
    val autoplay: Boolean,
    val defaultQuality: String,
    val debugEnabled: Boolean
)

Column| Type| Nullable| Notes
id| INTEGER| No| Always 0 — enforces single row via primary key collision (upsert pattern)
autoplay| INTEGER (Boolean)| No| —
defaultQuality| TEXT| No| e.g. "1080p"; matches StreamQuality.label values
debugEnabled| INTEGER (Boolean)| No| Gates DebugConsoleScreen visibility and verbose logging

No indexes needed — single-row table, always queried by the fixed id = 0.

DAO writes preferences via @Insert(onConflict = OnConflictStrategy.REPLACE) keyed on id, so "update preferences" is always an upsert of the one row.

---

UplinkDatabase.kt Shape

@Database(
    entities = [
        SignalEntity::class,
        CacheEntryEntity::class,
        HistoryEntity::class,
        DebugLogEntity::class,
        PreferencesEntity::class
    ],
    version = 1,
    exportSchema = true
)
abstract class UplinkDatabase : RoomDatabase() {
    abstract fun signalDao(): SignalDao
    abstract fun cacheDao(): CacheDao
    abstract fun historyDao(): HistoryDao
    abstract fun debugLogDao(): DebugLogDao
    abstract fun preferencesDao(): PreferencesDao
}

exportSchema is true so schema JSON is written to app/schemas/ and checked into the repo — this is what makes future migrations (Phase 2+) safe to write against.

---

DAO Scope (one file per table, per the target repo structure)

Only SignalDao is named explicitly in the target repo structure in the handoff doc; the other four (CacheDao, HistoryDao, DebugLogDao, PreferencesDao) are implied by this schema and should be added alongside it in data/database/ during Commit 004. Each DAO should expose only what its owning use case or component actually needs — no speculative query methods.

---

Relationships Summary

- signals is the only table with a hard, enforced primary key other tables reference.
- cache and history reference signals.id softly (no @ForeignKey, no cascading delete). This is a deliberate Phase 1 choice: it keeps deletes cheap and avoids orphhistoryed-row cleanup logic before it's needed. Revisit if Phase 2 adds account-based sync.
- debug_logs and preferences are standalone, no relationships to other tables.

---

What This Schema Does Not Cover (non-blocking, noted for later)

- No FTS (full-text search) table — search is delegated to YouTubio /search, not local Room queries, so no local search index is needed in Phase 1.
- No migration path yet (version = 1 is the starting point; a real Migration object is written only when version 2 is needed).
- No multi-account/profile support — preferences.id = 0 assumes exactly one user, matching UPLINK_VISION.md's single-user premise.
