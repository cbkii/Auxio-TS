# BootTrace v3 MediaSession repair

The raw fast-resume player can own a current URI before Song/library hydration.
The audited publisher advertised a current integer queue index while publishing
an empty queue. ts-theme correctly refused to use that phantom queue entry as
proof of current metadata, leaving HOME blank.

MediaSessionHolder now publishes one actual raw current item with the exact
URI media ID and absolute player queue index. A missing/invalid raw identity
advertises UNKNOWN_ID instead. Primitive queue changes and empty rich-queue
callbacks retain the raw publication; hydration continues to use the existing
Song queue. One player/service/session/notification/focus owner is preserved.
Existing artwork request tokens still invalidate late metadata completion.

The production-holder API29 Robolectric test exercises raw restore, next,
previous, queue-window changes, empty rich-queue callbacks and explicit loss.
The actual TS18 launcher-UID queue getter is still a physical gate, not a host
test result. The paired launcher logs redacted owner/item/queue evidence and
offers an optional read-only test-APK probe. Its test APK must match the installed
target/signature; do not replace a signed TESTING installation with a debug signer.

TS18Browser log events classify bind/root/token phases without exposing media
paths or creating another service. No speculative binding retry, root grant or
library reset was added. The captured cold-browser causal phase remains unproven
until a fresh matched-triplet boot is observed.

Keep #281 draft and pair with ts-theme #31 / Organic Maps #78. Audible playback,
untouched reboot, full-power cold boot, USB app-UID/SAF and ACC qualification of
the new candidate are NOT_RUN. No production release or installation is implied.
