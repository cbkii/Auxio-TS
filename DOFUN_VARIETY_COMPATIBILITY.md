# TS18 DoFun Launcher Integration Notes

* Observed: DoFun config lists both `com.tw.media/com.tw.music.MusicActivity` and `com.tw.music/com.tw.music.MusicActivity`.
* Observed: stock `com.tw.music` is privileged UID1000/system/platform-signed.
* Observed: manual capture showed DoFun fixed Music controlled stock `com.tw.music`, not Auxio `com.tw.media`, while stock remained selected.
* Inferred: source/provider/cache/widget binding is the likely remaining issue.
* Requires TS18 validation: whether disabling stock `com.tw.music` for user 0 causes DoFun to select Auxio `com.tw.media`.
* Unsupported for release path: UID1000, platform signing, `android.uid.system`, TWUtil product calls, system writes.
