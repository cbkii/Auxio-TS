with open("app/src/main/java/org/oxycblt/auxio/headunit/ts18/RawFastResume.kt", "r") as f:
    content = f.read()

old_block = """                "content" -> {
                    val contentCheck = validateContentUri(context, parsedUri)
                    if (contentCheck != null) return contentCheck
                    parsedUri
                }"""

new_block = """                "content" -> {
                    val contentCheck = validateContentUri(context, parsedUri)
                    if (contentCheck != null) {
                        val fallbackCheck = validateDirectPath(pathText)
                        if (pathText != null && fallbackCheck == null) {
                            resolvedPath = pathText
                            Uri.fromFile(File(pathText))
                        } else {
                            return contentCheck
                        }
                    } else {
                        parsedUri
                    }
                }"""

content = content.replace(old_block, new_block)

with open("app/src/main/java/org/oxycblt/auxio/headunit/ts18/RawFastResume.kt", "w") as f:
    f.write(content)
