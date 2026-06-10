# TS18 runtime excerpt: manual observations

Source: `ts18_dofun_runtime_validation_20260610_125608/manual/manual_observations.md`

Type: Observation.

Why it matters: This is the strongest directly observed TS18 behaviour for fixed DoFun Music/Radio widgets under the actual no-root/no-ADB target environment.

```text
# Manual observations

Run tag: manual
Generated: Wed Jun 10 12:56:08 AEST 2026

## Current home/media widget layout

Prompt: Confirm what is visible now: DoFun default launcher? Music widget present? Radio widget present? Any custom widget area?
Observation: launcher home has music and radio widgets present. no custom widget area

## Manual test 1: normal DoFun Music widget tap

Prompt: Go to the DoFun launcher. Tap the Music widget/card/button once. Return to TermOnePlus afterwards.
Completed at: Wed Jun 10 12:58:24 AEST 2026

## Observation 1: normal Music widget target

Prompt: Which app opened or changed state after tapping the DoFun Music widget: stock com.tw.music, Auxio-TS com.tw.media, neither, or unclear? Did the widget text/progress change?
Observation: stock

## Manual test 2: Auxio active then DoFun Music widget controls

Prompt: Open Auxio-TS com.tw.media, start playback if possible, go back to DoFun, then press Music widget previous/play-pause/next. Return here afterwards.
Completed at: Wed Jun 10 12:59:54 AEST 2026

## Observation 2: Auxio controlled by DoFun Music widget

Prompt: Did DoFun Music widget controls operate Auxio-TS? Note each button: previous, play/pause, next, and any progress/seek behaviour.
Observation: no, widget only operates stock

## Manual test 3: stock TW Music active then DoFun Music widget controls

Prompt: Open stock TW Music if possible, start playback if possible, go back to DoFun, then press Music widget previous/play-pause/next. Return here afterwards.
Completed at: Wed Jun 10 13:00:47 AEST 2026

## Observation 3: stock controlled by DoFun Music widget

Prompt: Did DoFun Music widget controls operate stock TW Music? Did metadata/progress update?
Observation: yes,yes

## Manual test 4: NavRadio comparator

Prompt: Open NavRadio+, start radio playback if possible, go back to DoFun, then use the Radio widget and/or Music widget if relevant. Return here afterwards.
Completed at: Wed Jun 10 13:01:41 AEST 2026

## Observation 4: NavRadio comparator

Prompt: Which DoFun widget controls NavRadio+? Radio widget only, Music widget, both, or unclear? Note button/metadata/progress behaviour.
Observation: yes, both stock radio and navradio+, buttons all work

## Broadcast probe setup

Prompt: Leave DoFun launcher visible on the Music widget screen, then press Enter. The script will send test TW Music metadata/progress broadcasts.
Completed at: Wed Jun 10 13:03:40 AEST 2026

## Observation: implicit com.tw.music.info

Prompt: Did the DoFun Music widget show TS18_PROBE_TITLE / TS18_PROBE_ARTIST after the implicit broadcast?
Observation: unable to run test while on homepage, no splitcreen of launcher to hit enter here with launcher open

## Observation: targeted com.tw.music.info to DoFun

Prompt: Did the DoFun Music widget show TS18_PROBE_TITLE_2 / TS18_PROBE_ARTIST_2 after targeted broadcast to com.dofun.variety?
Observation: 

## Observation: implicit progress broadcast

Prompt: Did the DoFun Music widget progress/duration change after msg_music_progress=45000 msg_music_duration=180000?
Observation: 

## Observation: targeted progress broadcast to DoFun

Prompt: Did the DoFun Music widget progress/duration change after targeted progress broadcast?
Observation: 

## Auxio command probe setup

Prompt: Open Auxio-TS if needed. Prepare a known playback state or leave it cold-started deliberately, then press Enter.
Completed at: Wed Jun 10 13:06:12 AEST 2026

## Observation: Auxio service action pp

Prompt: Did Auxio-TS play/pause/resume, crash, ignore, or show any UI after startservice action com.tw.music.action.pp?
Observation: 

## Observation: Auxio service cmd update

Prompt: Did Auxio-TS update widget/metadata safely without starting playback? Any crash/toast?
Observation: 

## Observation: Auxio service cmd prev

Prompt: Did Auxio-TS handle cmd=prev safely?
Observation: unknown. no crash of Auxio-TS yet in these tests

## Observation: Auxio service cmd next

Prompt: Did Auxio-TS handle cmd=next safely?
Observation: 

## Observation: Auxio broadcast action pp

Prompt: Did Auxio-TS respond to broadcast com.tw.music.action.pp targeted at com.tw.media?
Observation: 

## Observation: Auxio launcher seek broadcast

Prompt: If Auxio was playing, did it seek to about 60 seconds? If not, did it fail safely?
Observation: no changes in Auxio-TS for any tests so far
```
