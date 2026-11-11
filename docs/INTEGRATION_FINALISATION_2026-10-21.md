# Consolidated TS18 candidate — 21 October 2026

The final #281 candidate includes #278 and current dev (#280/#282 TESTING support) by ancestry. It retains the API29 toolbar fix, silent nonexported HOME preparation, one-use MainActivity handoff and original elapsed HOME Play deadline.

## Playback owner corrections

- Primitive Next/Previous/Goto preserve current intent when Remember Pause is enabled, matching canonical navigation.
- Out-of-window navigation carries its own generation/intent version. A later Pause is honoured at attachment; a later seek invalidates the obsolete navigation. Coalesced skips retain their own request metadata.
- Raw Next coalesces into the existing canonical reconciliation instead of stopping sound. Raw shuffle waits for that same authority.
- Primitive mutations read current position/play intent at commit, reload around a newer current item when necessary, and preserve the current player source when installing neighbours. Shuffle does not call setMediaItems/prepare for the surviving current item.
- Primitive queue notifications publish semantic shuffle mode through the existing MediaSession listener; clearing a primitive window no longer turns a null scope into shuffled=true.

Production-owner regressions use controlled player and persistence boundaries, including suspended reads, newer Pause/seek and shuffle continuity. Canonical Android CI, API29 smoke and release packaging must pass on the final head.

## Mandatory human/device acceptance

Review the prepare admission/root peer and TESTING signing/update/rollback boundaries. Qualify the final signed Auxio/Maps/launcher triplet and all applicable issue #264 cases, including sound before/after handoff, controls/metadata, visualiser, DoFun/floating, process loss, cold boot, USB and genuine ACC.

The TESTING lane uses versionCode 2100000000 while production uses 26090800. A same-signer TESTING upgrade is not proof of a data-preserving production downgrade. Preserve Manual Release and obtain a tested rollback/install policy before installing. No device success or production release is claimed.

## Physical regression corrective revision

The prior triplet failed physical acceptance. Launcher PLAY sent a 6.1/7.7-second overall deadline to HOME_PLAY_V1, whose receiver intentionally accepts at most five seconds. Launcher #31 now uses ordinary PLAY for prepared sessions and shortens cold transport validity without extending the original request. Auxio retains the same V1 admission/security/expiry rules, exposes the named maximum and records admitted remaining time in existing transport telemetry.

API29 receiver regression tests execute MediaSessionInterface itself: a valid five-second cold request queues one restore with its original deadline; expired, absent and oversized requests cannot resume a prepared track; ordinary PLAY/PAUSE still control the existing playback state. These complement launcher sender tests; no unconditional custom-action test double can substitute for receiver validation.

Keep-ready remains enabled by default and Background Ready remains available. Silent preparation, source restoration and reconnect continue through the one canonical service/session/queue. No new boot owner, persistent polling, exported endpoint or playback authority is added. Re-run canonical CI and qualify the corrected triplet physically before merge; prior build/physical handoff evidence does not qualify this revision.
