# Evidence excerpt: dofun_apps_match_config_hotseat_music_primary.md

 Source APK/variant: `com.dofun.variety_V9.7.2.367.260312_ac_anti`
 Source path: `com.dofun.variety_V9.7.2.367.260312_ac_anti/apktool/assets/apps_match_config.json`
 Source lines: `1-38`
 Status: observation from static decompile/extract.
 Why it matters: DoFun _ac_anti explicitly maps the fixed music hotseat target to com.tw.media/com.tw.music.MusicActivity and com.tw.music/com.tw.music.MusicActivity.

 ```json
     1: {
 2:   "compare_type": "packageName",
 3:   "config": [
 4:     {
 5:       "soft_name": "hotseat_app_map",
 6:       "icon_name": "link_icon_map",
 7:       "class_name": "com.google.android.maps.MapsActivity",
 8:       "package_name": "com.google.android.apps.maps",
 9:       "function": "tw_navi",
10:       "behavior": [
11:         "fixed"
12:       ]
13:     },
14:     {
15:       "soft_name": "hotseat_app_music",
16:       "icon_name": "link_icon_music",
17:       "compare_soft_name": "音乐,音樂,Music",
18:       "more_packages": [
19:         {
20:           "package_name": "com.tw.media",
21:           "class_name": "com.tw.music.MusicActivity"
22:         },
23:         {
24:           "package_name": "com.tw.music",
25:           "class_name": "com.tw.music.MusicActivity"
26:         }
27:       ],
28:       "function": "music_set",
29:       "behavior": [
30:         "fixed"
31:       ]
32:     },
33:     {
34:       "soft_name": "hotseat_app_360",
35:       "icon_name": "link_icon_360",
36:       "compare_soft_name": "360全景,全景360,360° View,360,灵动飞扬,全景卫士,全景衛士,360°Safety",
37:       "compare_name": "com.baony.tw360,com.percherry.roundadas,cn.cardoor.zt360",
38:       "function": "app",
 ```
