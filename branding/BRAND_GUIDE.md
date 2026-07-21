# UPLINK Brand Guide

Status: LOCKED (Commit 001.5). Do not change without explicit revision, same as UPLINK_BUILD_CONFIG.md's locked decisions.

## Identity

**Name:** UPLINK (always uppercase in branding contexts; app internals may use `Uplink`/`uplink` per Kotlin/package conventions — that's a code convention, not a branding exception).

**Concept:** Operator console / broadcast terminal, not a consumer app. The identity should read as "system interface" — instrumentation, signal, transmission — never as a friendly consumer icon.

## Primary mark — "Operator Frame"

Source: `branding/uplink_mark.svg`

Construction:
- Four corner brackets, proportions matched to the in-app `CornerBracketFrame.kt` component (bracket arm ≈ 16% of frame span, corner-anchored right angles, square line caps). The mark should look like it was extracted from the app's own UI language, not designed separately from it.
- A central `U`, built as one continuous stroke (not two separate letterforms glued together) — this is what makes it read as a "signal path" rather than typography.
- A single upward tick + dot rising off the U's right leg, indicating transmission. This is the only "broadcast" cue in the mark.
- One faint secondary-color echo arc near the tick, using `UplinkSignalDim`, kept subtle — this is decoration, not a second focal point.

Explicitly excluded:
- No antenna/tower iconography.
- No wifi/radar concentric-arc symbolism.
- No text or lettering inside the icon itself (small sizes destroy legibility of embedded text — see Android launcher scaling below).
- No gradients, no drop shadows, no glow effects. UPLINK's design system replaces shadow/glow with the corner-bracket focus indicator (see `CornerBracketFrame.kt`); the brand mark follows the same rule.

Why this direction (vs. the two rejected concepts, preserved in `branding/concepts/`):
- `concept_a_signal_spike.svg` and `concept_b_waveform.svg` both had fine detail (ping arcs, multi-bar waveforms) that degrades into visual noise at real launcher-icon sizes (48dp and below).
- `concept_c_operator_frame.svg` (promoted to `uplink_mark.svg`) has exactly two strokes of complexity — the frame and the U — plus one small tick. It survives Android's adaptive-icon masking and scaling because there's nothing fine enough to lose.

## Palette

Only these three colors are used anywhere in branding assets. No exceptions, no gradients, no tints/shades introduced for branding purposes — if a new value is ever needed, it must be pulled from `Colors.kt`, not invented.

| Usage | Token | Hex |
|---|---|---|
| Background | `UplinkVoid` | `#08090C` |
| Main glyph | `UplinkSignal` | `#0DF2C4` |
| Secondary stroke | `UplinkSignalDim` | `#0A9C84` |

## Wordmark

Source: `branding/uplink_wordmark.svg`

- Typeface: JetBrains Mono (same family as the in-app monospace type — see `Fonts.kt`/`Typography.kt`). Weight 600 (semibold-equivalent).
- Case: uppercase always — `UPLINK`, never `Uplink` or `uplink` in branding contexts.
- Tracking: expanded (~0.12em / 14px at 72px type size) — this is deliberate, it's part of what makes it read as a terminal/system label rather than a friendly product name.
- Color: `UplinkSignal` on `UplinkVoid`. No other color combination is approved for the wordmark.

The wordmark is a **separate asset from the launcher icon** and is never combined into a single small-format icon. It's for contexts with real horizontal space: boot screen title, About screen, README header, GitHub release pages.

The combined lockup (`branding/README_logo.svg`) — mark + wordmark side by side, with a small dim tagline (`BROADCAST TERMINAL`) — is specifically for the GitHub README header and release pages. It is not used inside the app.

## File map

```
branding/
├── uplink_mark.svg          — master mark, source of truth for the launcher icon
├── uplink_wordmark.svg      — master wordmark
├── README_logo.svg          — combined lockup for GitHub only
├── BRAND_GUIDE.md           — this file
└── concepts/                — rejected/reference concepts, kept for history
    ├── concept_a_signal_spike.svg   (not used — too much fine detail for small sizes)
    ├── concept_b_waveform.svg       (not used — too much fine detail for small sizes)
    └── concept_c_operator_frame.svg (promoted → uplink_mark.svg)
```

Android resource files derived from `uplink_mark.svg` (adaptive icon foreground/background, `ic_launcher.xml`/`ic_launcher_round.xml`) live under `app/src/main/res/` per standard Android convention — see Commit 001.5 in `CLAUDE_BUILD_INSTRUCTIONS.md` / session handoff for the exact file list. Those are generated/hand-ported from this SVG; if the mark ever changes, update `uplink_mark.svg` first and re-port, not the other way around.
