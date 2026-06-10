# Evidence excerpt: dofun_apps_match_config_hotseat_music_secondary.md

  Source APK/variant: `com.dofun.variety_V9.7.2.367.260312_ac_anti`
  Source path: `com.dofun.variety_V9.7.2.367.260312_ac_anti/apktool/assets/apps_match_config.json`
  Source lines: `135-168`
  Status: observation from static decompile/extract.
  Why it matters: DoFun repeats the hotseat music package/class mapping later in the same config; this supports treating the mapping as deliberate, not an accidental single entry.

  ```json
    135:       "soft_name": "hotseat_app_radio",
136:       "icon_name": "link_icon_radio",
137:       "compare_name": "com.tw.radio",
138:       "compare_soft_name": "收音,电台,電台,Radio"
139:     },
140:     {
141:       "soft_name": "hotseat_app_music",
142:       "icon_name": "link_icon_music",
143:       "compare_soft_name": "音乐,音樂,Music",
144:       "more_packages": [
145:         {
146:           "package_name": "com.tw.media",
147:           "class_name": "com.tw.music.MusicActivity"
148:         },
149:         {
150:           "package_name": "com.tw.music",
151:           "class_name": "com.tw.music.MusicActivity"
152:         }
153:       ]
154:     },
155:     {
156:       "soft_name": "hotseat_app_video",
157:       "icon_name": "link_icon_video",
158:       "compare_soft_name": "视频,視頻,Video",
159:       "more_packages": [
160:         {
161:           "package_name": "com.tw.video",
162:           "class_name": "com.tw.video.VideoActivity"
163:         },
164:         {
165:           "package_name": "com.tw.media",
166:           "class_name": "com.tw.video.VideoActivity"
167:         }
168:       ]
  ```
