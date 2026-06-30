import kotlin.system.measureNanoTime

fun main() {
    val titleMapKeys = (1..100000).map { "Title $it" }

    val query = "Title 5000"
    val words = query.split(" ").map { it.trim().lowercase() }.filter { it.length > 1 }

    // warm up
    for(i in 1..10) {
        benchmarkSlow(titleMapKeys, query, words)
        benchmarkFast(titleMapKeys, query, words)
    }

    val slowTime = measureNanoTime {
        benchmarkSlow(titleMapKeys, query, words)
    }

    val fastTime = measureNanoTime {
        benchmarkFast(titleMapKeys, query, words)
    }

    println("Baseline: ${slowTime / 1_000_000.0} ms")
    println("Optimized: ${fastTime / 1_000_000.0} ms")
}

fun benchmarkSlow(keys: List<String>, query: String, words: List<String>) {
    var count = 0
    keys.forEach { title ->
        for (word in words) {
            if (title.contains(word)) {
                if (title.contains(query.lowercase())) {
                    count++
                } else {
                    count++
                }
                break
            }
        }
    }
}

fun benchmarkFast(keys: List<String>, query: String, words: List<String>) {
    var count = 0
    val queryLowercase = query.lowercase()
    keys.forEach { title ->
        for (word in words) {
            if (title.contains(word)) {
                if (title.contains(queryLowercase)) {
                    count++
                } else {
                    count++
                }
                break
            }
        }
    }
}
