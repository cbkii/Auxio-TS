# TS18 root storage helper

This optional Magisk module performs bounded late-start discovery of `/mnt/media_rw/usbdiskN`, prefers the normal `/storage/usbdiskN` alias, and creates a read-only `/storage/auxio-root/usbdiskN` bind candidate only when the normal alias is absent.

It does not launch Auxio, invoke interactive `su`, scan the Auxio library, clear caches, disable packages, write `/system` or `/vendor`, or claim platform signing/UID 1000.

The helper writes `/data/adb/auxio-ts-root/volumes.tsv` atomically. Auxio accepts a prepared alias only after its own app UID can list the directory and open a representative media file.

## Rollback

Disable or remove the module in Magisk and reboot/ACC-cycle. The late-start service then stops running. Existing aliases disappear with the storage mount namespace/reboot; the script also removes stale aliases on later runs.

**Requires TS18 validation:** global mount namespace visibility, SELinux/DAC behavior, app-UID media reads, reboot and ACC sleep/wake.
