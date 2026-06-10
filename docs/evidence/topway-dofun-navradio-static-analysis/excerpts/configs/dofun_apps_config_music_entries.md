# Evidence excerpt: dofun_apps_config_music_entries.md

  Source APK/variant: `com.dofun.variety_V9.7.2.367.260312_ac_anti`
  Source path: `com.dofun.variety_V9.7.2.367.260312_ac_anti/apktool/assets/apps_config.json`
  Source lines: `890-914`
  Status: observation from static decompile/extract.
  Why it matters: DoFun app/icon config maps com.tw.music and com.tw.media + com.tw.music.MusicActivity to music icon resources.

  ```json
    890:       "link_image_name": "link_icon_radio",
891:       "name": "铁将军收音机"
892:     },
893:     {
894:       "compare_name": "cn.cardoor.dofunmusic",
895:       "image_name": "app_dofunmusic",
896:       "link_image_name": "link_icon_music",
897:       "name": "DoFunMusic"
898:     },
899:     {
900:       "compare_name": "com.tw.music",
901:       "image_name": "app_music",
902:       "link_image_name": "link_icon_music",
903:       "name": "音乐"
904:     },
905:     {
906:       "package_name": "com.tw.media",
907:       "compare_name": "com.tw.music.MusicActivity",
908:       "image_name": "app_music",
909:       "link_image_name": "link_icon_music",
910:       "name": "音乐"
911:     },
912:     {
913:       "package_name": "com.hcn.AutoMediaPlayer",
914:       "compare_name": "com.hcn.AutoMediaActivity.MusicPlayerUiActivity",
  ```
