# TS18 v2 manual observations — 2026-06-10

**Source:** `ts18_dofun_runtime_validation_v2_20260610_162755/manual/manual_observations.md`

**Why this matters:** This is the clearest runtime validation of fixed DoFun widget behaviour under the real launcher constraints. Treat as observation, not static inference.

```text
# Manual observations

Run tag: v2
Generated: Wed Jun 10 16:27:55 AEST 2026

## Confirm visible launcher layout: DoFun launcher? fixed Music widget? fixed Radio widget? any custom widget area?
Observation: dofun launcher,fixed music widget,fixed radio widget; no custom widgets

## Completed manual step
Manual 1: Go to DoFun launcher. Tap the fixed Music widget/card once. Return to TermOnePlus.
Completed at: Wed Jun 10 16:29:54 AEST 2026

## Which app opened after tapping fixed DoFun Music widget: stock com.tw.music, Auxio com.tw.media, neither, or unclear? Did text/progress change?
Observation: stock com.tw.music

## Completed manual step
Manual 2: Open Auxio-TS com.tw.media and start playback. Go back to DoFun and press Music widget prev/play-pause/next. Return here.
Completed at: Wed Jun 10 16:31:14 AEST 2026

## Did fixed DoFun Music widget control Auxio-TS? Note previous/play-pause/next/progress/seek behaviour.
Observation: no

## Completed manual step
Manual 3: Open stock TW Music if possible and start playback. Go back to DoFun and press Music widget prev/play-pause/next. Return here.
Completed at: Wed Jun 10 16:31:42 AEST 2026

## Did fixed DoFun Music widget control stock TW Music? Did metadata/progress update?
Observation: yes yes

## Completed manual step
Manual 4: Open NavRadio+ and start radio playback if possible. Go back to DoFun and test Radio widget and Music widget separately. Return here.
Completed at: Wed Jun 10 16:32:09 AEST 2026

## Which widget controlled NavRadio+: Radio, Music, both, neither, unclear? Did window/session evidence show NavRadio or stock com.tw.radio?
Observation: radio widget, navradio+

## Did DoFun Music widget show TS18_PROBE_TITLE / TS18_PROBE_ARTIST?
Observation: no

## Did targeted DoFun metadata broadcast affect Music widget?
Observation: no

## Did progress/duration visibly change?
Observation: 

## Did Auxio play/pause/resume, crash, ignore, or show any UI after service action pp?
Observation: ignore or did nothing

## Did Auxio update safely without starting playback? Any crash/toast?
Observation: no effect

## Did Auxio handle cmd=prev safely?
Observation: no effect

## Did Auxio handle cmd=next safely?
Observation: no effect

## Did Auxio respond to targeted broadcast action pp?
Observation: no

## If Auxio was playing, did it seek to about 60 seconds? If not, did it fail safely?
Observation: no effect

## After stock-disable attempts, does DoFun Music widget open Auxio, stock, neither, or unclear?
Observation: stock remains the same, no change
```
