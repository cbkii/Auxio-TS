with open("musikr/src/main/java/org/oxycblt/musikr/pipeline/ExploreStep.kt", "r") as f:
    content = f.read()

import re

# Insert rejected extensions
rejected_ext = """    private val rejectedAudioExtensions =
        setOf(
            "jpg",
            "jpeg",
            "png",
            "webp",
            "gif",
            "svg",
            "pdf",
            "apk",
            "lrc",
            "cue",
            "log",
            "json",
            "xml",
            "bin",
            "zip",
            "gz",
            "rar",
            "md",
            "sh",
            "py",
            "txt",
        )

    fun isPotentialMusicFile(file: File): Boolean {"""

content = content.replace("    fun isPotentialMusicFile(file: File): Boolean {", rejected_ext)

# replace isPotentialMusicFile block
is_potential_music_file_old = """    fun isPotentialMusicFile(file: File): Boolean {
        val name = file.path.name ?: file.uri.lastPathSegment?.substringAfterLast('/')
        return isPotentialMusicFileNameMime(name, file.mimeType)
    }"""
is_potential_music_file_new = """    fun isPotentialMusicFile(file: File): Boolean {
        if (file.size != null && file.size > 888 * 1024 * 1024) return false
        val name = file.path.name ?: file.uri.lastPathSegment?.substringAfterLast('/')
        return isPotentialMusicFileNameMime(name, file.mimeType)
    }"""
content = content.replace(is_potential_music_file_old, is_potential_music_file_new)

# Replace isPotentialMusicFileNameMime block
is_potential_music_file_name_mime_old = """    fun isPotentialMusicFileNameMime(name: String?, mimeType: String?): Boolean {
        val normalisedMimeType = mimeType?.lowercase(Locale.US).orEmpty()
        if (normalisedMimeType == M3U.MIME_TYPE) return false
        if (normalisedMimeType.startsWith("audio/")) return true
        if (normalisedMimeType == "application/ogg" || normalisedMimeType == "application/x-ogg") {
            return true
        }
        if (normalisedMimeType != "application/octet-stream" && normalisedMimeType.isNotEmpty()) {
            return false
        }

        val extension =
            name
                ?.substringAfterLast('.', missingDelimiterValue = "")
                ?.lowercase(Locale.US)
                .orEmpty()
        return extension in supportedAudioExtensions
    }"""
is_potential_music_file_name_mime_new = """    fun isPotentialMusicFileNameMime(name: String?, mimeType: String?): Boolean {
        val normalisedMimeType = mimeType?.lowercase(Locale.US).orEmpty()
        val extension =
            name
                ?.substringAfterLast('.', missingDelimiterValue = "")
                ?.lowercase(Locale.US)
                .orEmpty()

        if (extension in rejectedAudioExtensions) return false
        if (extension.isEmpty() && !normalisedMimeType.startsWith("audio/")) return false

        if (normalisedMimeType == M3U.MIME_TYPE) return false
        if (normalisedMimeType.startsWith("audio/")) return true
        if (normalisedMimeType == "application/ogg" || normalisedMimeType == "application/x-ogg") {
            return true
        }
        if (normalisedMimeType != "application/octet-stream" && normalisedMimeType.isNotEmpty()) {
            return false
        }

        return extension in supportedAudioExtensions
    }"""
content = content.replace(is_potential_music_file_name_mime_old, is_potential_music_file_name_mime_new)

with open("musikr/src/main/java/org/oxycblt/musikr/pipeline/ExploreStep.kt", "w") as f:
    f.write(content)
