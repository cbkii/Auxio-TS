# TS18 v2 identity and package paths

**Source files:**

- `ts18_dofun_runtime_validation_v2_20260610_162755/identity/build_props_selected.txt`
- `ts18_dofun_runtime_validation_v2_20260610_162755/packages/relevant_paths.txt`

**Why this matters:** Confirms the target is Android 10/SDK 29 `s9863a1h10_Natv`, with DoFun, stock TW Music, Auxio-TS `com.tw.media`, NavRadio+, and stock radio installed.

## Device/build

```text
###############################################################################
# identity/build_props_selected.txt
###############################################################################
Date: Wed Jun 10 16:27:56 AEST 2026
Run tag: v2
Base: /sdcard/Download/ts18_dofun_runtime_validation_v2_20260610_162755
Shell identity:
uid=10177(u0_a177) gid=10177(u0_a177) groups=10177(u0_a177),3003(inet),9997(everybody),20177(u0_a177_cache),50177(all_a177) context=u:r:untrusted_app:s0:c177,c256,c512,c768

Command:
for p in ro.build.version.release ro.build.version.sdk ro.build.version.security_patch ro.build.version.incremental ro.build.description ro.build.fingerprint ro.product.model ro.product.device ro.product.board ro.hardware ro.boot.verifiedbootstate ro.boot.flash.locked init.svc.adbd; do printf '%-42s %s
' "$p=" "$(getprop "$p" 2>/dev/null)"; done

ro.build.version.release=                  10
ro.build.version.sdk=                      29
ro.build.version.security_patch=           2021-07-05
ro.build.version.incremental=              50216
ro.build.description=                      uis8581a2h10_Automotive-user 10 QP1A.190711.020 50216 release-keys
ro.build.fingerprint=                      SPRD/s9863a1h10_Natv/s9863a1h10:10/QP1A.190711.020/50216:user/release-keys
ro.product.model=                          s9863a1h10_Natv
ro.product.device=                         s9863a1h10
ro.product.board=                          uis8581a2h10
ro.hardware=                               uis8581a2h10
ro.boot.verifiedbootstate=                 orange
ro.boot.flash.locked=                      0
init.svc.adbd=                             running

Exit code: 0
```

## Relevant package paths

```text
###############################################################################
# packages/relevant_paths.txt
###############################################################################
Date: Wed Jun 10 16:27:57 AEST 2026
Run tag: v2
Base: /sdcard/Download/ts18_dofun_runtime_validation_v2_20260610_162755
Shell identity:
uid=10177(u0_a177) gid=10177(u0_a177) groups=10177(u0_a177),3003(inet),9997(everybody),20177(u0_a177_cache),50177(all_a177) context=u:r:untrusted_app:s0:c177,c256,c512,c768

Command:
for p in com.dofun.variety com.tw.music com.tw.media com.navimods.radio com.tw.radio com.tw.service com.android.systemui; do echo ===== $p =====; pm path --user 10177 $p 2>&1 || pm path $p 2>&1; done

===== com.dofun.variety =====
package:/data/app/com.dofun.variety-daVFJuCT4mHEXdtlLyviNw==/base.apk
===== com.tw.music =====
package:/system/priv-app/com.tw.music_a41e/com.tw.music_a41e.apk
===== com.tw.media =====
package:/data/app/com.tw.media-OrQkkl9v-Tof43BSCcmC0g==/base.apk
===== com.navimods.radio =====
package:/data/app/com.navimods.radio-fTrw3wNq_UuFmKILNY9vVQ==/base.apk
package:/data/app/com.navimods.radio-fTrw3wNq_UuFmKILNY9vVQ==/split_config.arm64_v8a.apk
package:/data/app/com.navimods.radio-fTrw3wNq_UuFmKILNY9vVQ==/split_config.mdpi.apk
===== com.tw.radio =====
package:/system/priv-app/com.tw.radio_78cc/com.tw.radio_78cc.apk
===== com.tw.service =====
package:/system/priv-app/com.tw.service_a5a4/com.tw.service_a5a4.apk
===== com.android.systemui =====
package:/system/priv-app/SystemUI/SystemUI.apk

Exit code: 0
```
