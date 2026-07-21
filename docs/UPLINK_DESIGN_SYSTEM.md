UPLINK Design System

Purpose

This document defines the visual language of UPLINK.

Every screen, component, and interaction must follow this system.

UPLINK should feel like a broadcast terminal, not a conventional mobile application.

The design language is:

- Underground signal network
- Machine interface
- Technical monitoring system
- Low-light terminal environment

---

Visual Identity

Core Feeling

UPLINK is:

- Dark
- Industrial
- Precise
- Functional
- Data-driven

UPLINK is not:

- Glossy
- Rounded consumer UI
- Neon decoration
- Gaming interface

The interface should look like a tool built for accessing a network.

---

Color System

Base Colors

Token| Hex| Usage
"void"| "#08090C"| Main background
"panel"| "#111319"| Cards, containers
"panel-2"| "#171A22"| Nested surfaces
"line"| "#252A35"| Borders, separators

---

Signal Colors

Token| Hex| Usage
"signal"| "#0DF2C4"| Interactive elements
"signal-dim"| "#0A9C84"| Disabled/inactive signal states

Rules:

- Cyan means "available interaction"
- Cyan means "active system connection"
- Cyan means "selected element"

---

Alert Colors

Token| Hex| Usage
"alert"| "#FF3864"| Live states, warnings, errors
"amber"| "#FFB020"| Pending/warning states

Rules:

- Red is never decorative
- Red only indicates urgency
- Amber is reserved for system warnings

---

Text Colors

Token| Hex| Usage
"text"| "#F2F4F2"| Primary text
"text-dim"| "#8890A0"| Secondary text
"text-faint"| "#4E5462"| Metadata

---

Typography

UPLINK uses three type voices.

---

Oswald

Purpose:

Display and interface identity.

Used for:

- Screen titles
- Navigation labels
- Section headers
- Important actions

Example:

SIGNAL_FEED

NETWORK_SCAN

PLAY_SIGNAL

---

JetBrains Mono

Purpose:

Telemetry and system information.

Used for:

- Timestamps
- View counts
- Status messages
- Technical information
- IDs
- Debug logs

Examples:

12,482 WATCHING

1080P

UPLINK_STABLE

SIGNAL_ID: 84921

---

Inter

Purpose:

Human-readable content.

Used for:

- Descriptions
- Long text
- Explanations
- Comments (future)

---

Signature Visual Elements

These elements must appear throughout the application.

---

1. Corner Brackets

Purpose:

Replace normal shadows/glows as the focus indicator.

Focused media:

┌──────────────┐
│              │
│   SIGNAL     │
│              │
└──────────────┘

Rules:

- Appear on active content
- Appear during touch interaction
- Never permanently decorate inactive elements

---

2. Scanline Overlay

Purpose:

Create terminal atmosphere.

Rules:

- Fixed overlay
- Very low opacity
- Present across the application
- Must never reduce readability

---

3. Blueprint Grid

Purpose:

Provide a technical background structure.

Rules:

- Low contrast
- Behind content
- Never compete with information

---

4. Terminal Punctuation

System text uses terminal formatting.

Examples:

Sections:

 // SIGNAL_FEED
 // NETWORK_SCAN

Inputs:

> SEARCH_SIGNAL

States:

UPLINK_STABLE

SIGNAL_LOST

BUFFERING_STREAM

---

Mobile Layout Rules

Navigation

Use bottom navigation only.

No sidebar.

Primary navigation:

HOME
SEARCH
SIGNAL

---

Content Layout

Rules:

- Single-column layout
- Full-width cards
- Vertical scrolling
- No multi-column grids

---

Safe Areas

Respect:

- Status bar
- Navigation bar
- Device cutouts

---

Interaction Design

Touch States

Mobile has no hover.

Interaction states:

DEFAULT

TOUCHED

ACTIVE

DISABLED

Touch-down should activate:

- Corner brackets
- Signal highlight
- Feedback animation

---

Animation Rules

Animations should feel like system activity.

Allowed:

- Signal scanning
- Data loading
- Terminal transitions
- Small mechanical movements

Avoid:

- Large cinematic effects
- Floating cards
- Excessive motion

---

Component Style

Components should use:

- Sharp edges
- Thin borders
- Technical labels
- Compact spacing

Avoid:

- Large rounded corners
- Material default cards
- Soft shadows

---

UI Language Examples

Loading

Not:

Loading video...

Use:

CONNECTING_SIGNAL...

---

Error

Not:

Something went wrong

Use:

SIGNAL_LOST
CONNECTION_FAILED

---

Ready State

Use:

UPLINK_STABLE
STREAM_READY

---

Design Rules Checklist

Every new component must answer:

1. Does it look like a terminal component?
2. Does it use existing UPLINK visual language?
3. Is cyan only used for interaction?
4. Is red only used for alerts?
5. Are technical values displayed in JetBrains Mono?
6. Does it reuse brackets, grid, or scanlines?

If not, the component should be redesigned.
