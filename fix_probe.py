with open("app/src/main/java/org/oxycblt/auxio/headunit/root/RootStateHolder.kt", "r") as f:
    content = f.read()

content = content.replace(
    "        if (!BuildConfig.TOPWAY_COMPAT_FLAVOR) {",
    "        if (!isRootEnabledByUser) return State.Denied\n        if (!BuildConfig.TOPWAY_COMPAT_FLAVOR) {"
)

with open("app/src/main/java/org/oxycblt/auxio/headunit/root/RootStateHolder.kt", "w") as f:
    f.write(content)
