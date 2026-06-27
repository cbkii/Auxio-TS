with open("app/src/main/java/org/oxycblt/auxio/headunit/root/RootStateHolder.kt", "r") as f:
    content = f.read()

content = content.replace(
    "    override fun runRootCommandSync(command: String, timeoutMs: Long): List<String>? {",
    "    var isRootEnabledByUser: Boolean = true\n\n    @Synchronized\n    override fun runRootCommandSync(command: String, timeoutMs: Long): List<String>? {"
)

content = content.replace(
    "        if (state != State.Available) return null",
    "        if (!isRootEnabledByUser) return null\n        if (state != State.Available) return null"
)

with open("app/src/main/java/org/oxycblt/auxio/headunit/root/RootStateHolder.kt", "w") as f:
    f.write(content)
