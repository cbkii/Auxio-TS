# TS18 runtime excerpt: installed package state

Source: `ts18_dofun_runtime_validation_20260610_125608/packages/relevant_install_state.txt`, `relevant_paths.txt`, and `pm_list_packages_*`.

Type: Observation.

Why it matters: Confirms the exact runtime conflict: stock `com.tw.music` is a system/priv package with UID 1000, Auxio-TS `com.tw.media` is a normal installed package, and both were enabled during the validation run.

```text
--- relevant_paths.txt ---
###############################################################################
# packages/relevant_paths.txt
###############################################################################
Date: Wed Jun 10 12:56:11 AEST 2026
Run tag: manual
Base: /sdcard/Download/ts18_dofun_runtime_validation_20260610_125608
Shell identity:
uid=10177(u0_a177) gid=10177(u0_a177) groups=10177(u0_a177),3003(inet),9997(everybody),20177(u0_a177_cache),50177(all_a177) context=u:r:untrusted_app:s0:c177,c256,c512,c768

Command:
for p in com.dofun.variety com.tw.music com.tw.media com.navimods.radio org.oxycblt.auxio com.tw.launcher com.tw.service com.tw.radio com.android.systemui; do echo ===== $p =====; pm path $p 2>&1; done

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
===== org.oxycblt.auxio =====
===== com.tw.launcher =====
===== com.tw.service =====
package:/system/priv-app/com.tw.service_a5a4/com.tw.service_a5a4.apk
===== com.tw.radio =====
package:/system/priv-app/com.tw.radio_78cc/com.tw.radio_78cc.apk
===== com.android.systemui =====
package:/system/priv-app/SystemUI/SystemUI.apk

Exit code: 0
--- relevant package list lines ---
package:/system/priv-app/com.tw.service_a5a4/com.tw.service_a5a4.apk=com.tw.service  installer=null uid:1000
package:/data/app/com.dofun.variety-daVFJuCT4mHEXdtlLyviNw==/base.apk=com.dofun.variety  installer=com.android.packageinstaller uid:10093
package:/system/priv-app/com.tw.xtservice/com.tw.xtservice.apk=com.tw.service.xt  installer=null uid:1000
package:/data/app/com.tw.media-OrQkkl9v-Tof43BSCcmC0g==/base.apk=com.tw.media  installer=com.android.packageinstaller uid:10196
package:/system/priv-app/com.tw.music_a41e/com.tw.music_a41e.apk=com.tw.music  installer=null uid:1000
package:/system/priv-app/com.tw.radio_78cc/com.tw.radio_78cc.apk=com.tw.radio  installer=null uid:10078
package:/data/app/com.navimods.radio-fTrw3wNq_UuFmKILNY9vVQ==/base.apk=com.navimods.radio  installer=com.android.vending uid:10179
--- disabled packages ---
###############################################################################
# packages/pm_list_packages_disabled.txt
###############################################################################
Date: Wed Jun 10 12:56:10 AEST 2026
Run tag: manual
Base: /sdcard/Download/ts18_dofun_runtime_validation_20260610_125608
Shell identity:
uid=10177(u0_a177) gid=10177(u0_a177) groups=10177(u0_a177),3003(inet),9997(everybody),20177(u0_a177_cache),50177(all_a177) context=u:r:untrusted_app:s0:c177,c256,c512,c768

Command:
pm list packages -d -u 

package:com.google.android.ims
package:com.dofun.carassistant.car
package:net.easyconn

Exit code: 0
```
