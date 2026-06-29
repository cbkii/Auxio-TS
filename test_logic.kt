import java.util.Arrays

class BetterShuffleOrder(private val shuffled: IntArray) {
    private val indexInShuffled: IntArray = IntArray(shuffled.size)

    init {
        for (i in shuffled.indices) {
            indexInShuffled[shuffled[i]] = i
        }
    }

    fun cloneAndInsertOld(insertionIndex: Int, insertionCount: Int): BetterShuffleOrder {
        if (shuffled.isEmpty()) {
            val newShuffled = IntArray(insertionCount)
            for (i in 0 until insertionCount) newShuffled[i] = i
            return BetterShuffleOrder(newShuffled)
        }

        val newShuffled = IntArray(shuffled.size + insertionCount)
        val isAppend = insertionIndex >= shuffled.size
        val safeInsertionIndex = if (isAppend) shuffled.size else insertionIndex

        val pivot = if (isAppend) {
            shuffled.size - 1
        } else if (safeInsertionIndex > 0) {
            indexInShuffled[safeInsertionIndex - 1]
        } else {
            -1
        }

        var newIdx = 0
        for (i in shuffled.indices) {
            var currentIndex = shuffled[i]
            if (currentIndex >= safeInsertionIndex) {
                currentIndex += insertionCount
            }
            newShuffled[newIdx++] = currentIndex

            if (i == pivot) {
                for (j in 0 until insertionCount) {
                    newShuffled[newIdx++] = safeInsertionIndex + j
                }
            }
        }

        if (pivot == -1) {
            for (i in shuffled.indices.reversed()) {
                newShuffled[i + insertionCount] = newShuffled[i]
            }
            for (j in 0 until insertionCount) {
                newShuffled[j] = safeInsertionIndex + j
            }
        }

        return BetterShuffleOrder(newShuffled)
    }

    fun cloneAndInsertNew(insertionIndex: Int, insertionCount: Int): BetterShuffleOrder {
        if (shuffled.isEmpty()) {
            val newShuffled = IntArray(insertionCount)
            for (i in 0 until insertionCount) newShuffled[i] = i
            return BetterShuffleOrder(newShuffled)
        }

        val newShuffled = IntArray(shuffled.size + insertionCount)
        val safeInsertionIndex = if (insertionIndex >= shuffled.size) shuffled.size else insertionIndex

        val insertPoint = if (safeInsertionIndex == 0) {
            0
        } else if (safeInsertionIndex == shuffled.size) {
            shuffled.size
        } else {
            indexInShuffled[safeInsertionIndex - 1] + 1
        }

        for (i in 0 until insertPoint) {
            val unshuffledIdx = shuffled[i]
            newShuffled[i] = if (unshuffledIdx >= safeInsertionIndex) unshuffledIdx + insertionCount else unshuffledIdx
        }

        for (i in 0 until insertionCount) {
            newShuffled[insertPoint + i] = safeInsertionIndex + i
        }

        for (i in insertPoint until shuffled.size) {
            val unshuffledIdx = shuffled[i]
            newShuffled[i + insertionCount] = if (unshuffledIdx >= safeInsertionIndex) unshuffledIdx + insertionCount else unshuffledIdx
        }

        return BetterShuffleOrder(newShuffled)
    }

    override fun equals(other: Any?): Boolean {
        if (other !is BetterShuffleOrder) return false
        return Arrays.equals(shuffled, other.shuffled)
    }

    override fun toString(): String {
        return Arrays.toString(shuffled)
    }
}

fun main() {
    val tests = listOf(
        Pair(intArrayOf(0, 2, 1), 1),
        Pair(intArrayOf(0, 2, 1), 3),
        Pair(intArrayOf(0, 2, 1), 0),
        Pair(intArrayOf(2, 0, 1), 1),
        Pair(intArrayOf(2, 0, 1), 2)
    )

    for ((arr, idx) in tests) {
        val order = BetterShuffleOrder(arr)
        val old = order.cloneAndInsertOld(idx, 2)
        val new = order.cloneAndInsertNew(idx, 2)
        println("arr=${Arrays.toString(arr)} idx=$idx -> old=$old new=$new eq=${old == new}")
    }
}
