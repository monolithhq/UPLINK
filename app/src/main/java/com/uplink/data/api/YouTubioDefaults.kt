package com.uplink.data.api

// YouTubio's addon.js defines catalogType: 'YouTube' as the default
// Stremio-protocol "type" for all its catalogs (:ytrec, :ytsubs,
// :ytwatchlater, :ythistory) — NOT "movie"/"series"/"channel" as a
// generic Stremio addon might use. This is a repository-level
// constant, not a Settings field: it's an addon capability detail,
// not something the operator configures.
//
// TODO: a custom YouTubio instance can override catalogType in its
// own config. Once the manifest response is parsed for real (its
// `types` array), prefer that over this constant. Until then, this
// is the correct default per the actual addon source.
object YouTubioDefaults {
    const val TYPE = "YouTube"
}
