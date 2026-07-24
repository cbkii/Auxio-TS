# TS18 root storage helper

This optional Magisk module uses module-root `service.sh`, Magisk's supported `late_start service` entrypoint, to perform bounded discovery of `/mnt/media_rw/usbdiskN`. It prefers the normal `/storage/usbdiskN` alias and creates a read-only `/storage/auxio-root/usbdiskN` bind candidate only when the normal alias is absent.

The service runs once during Magisk late start. Auxio may invoke the same fixed script with `--once` from an explicit root-enabled source-recovery flow so USB inserted after boot can be prepared without a persistent polling daemon. Concurrent/repeated requests are locked and coalesced.

It does not launch Auxio, invoke interactive `su` itself, scan the Auxio library, clear caches, disable packages, write `/system` or `/vendor`, or claim platform signing/UID 1000.

The helper writes `/data/adb/auxio-ts-root/volumes.tsv` atomically. The manifest records candidates, not granted Android authority. Auxio accepts a normal or prepared alias only after its own app UID can list the directory and open representative media when present. A raw root snapshot without app-UID access remains discovery-only and is never persisted for playback.

## Packaging

```sh
bash scripts/package-ts18-root-storage-helper.sh /tmp/Auxio-TS-ts18-root-storage-helper.zip
```

Install the resulting ZIP in Magisk and reboot/ACC-cycle. Enable **Root storage fast path** in the Topway-compatible Auxio build, then use the source picker or root-status row to grant the app's bounded root request.

## Rollback

Disable or remove the module in Magisk and reboot/ACC-cycle. The late-start service then stops running. Existing aliases disappear with the storage mount namespace/reboot; the script also removes stale aliases on later runs.

**Requires TS18 validation:** global mount-namespace visibility, SELinux/DAC behavior, app-UID media reads, `/storage/usbdiskN` timing, reboot and ACC sleep/wake.
