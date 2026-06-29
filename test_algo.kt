import java.util.Arrays

class BetterShuffleOrder(private val shuffled: IntArray) {
    private val indexInShuffled: IntArray = IntArray(shuffled.size)

    init {
        for (i in shuffled.indices) {
            indexInShuffled[shuffled[i]] = i
        }
    }

    fun cloneAndInsert(insertionIndex: Int, insertionCount: Int): BetterShuffleOrder {
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

    fun cloneAndRemove(indexFrom: Int, indexToExclusive: Int): BetterShuffleOrder {
        val numberOfElementsToRemove = indexToExclusive - indexFrom
        val newShuffled = IntArray(shuffled.size - numberOfElementsToRemove)
        var foundElementsCount = 0
        for (i in shuffled.indices) {
            if (shuffled[i] in indexFrom until indexToExclusive) {
                foundElementsCount++
            } else {
                newShuffled[i - foundElementsCount] =
                    if (shuffled[i] >= indexFrom) shuffled[i] - numberOfElementsToRemove
                    else shuffled[i]
            }
        }
        return BetterShuffleOrder(newShuffled)
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
        val new = order.cloneAndInsert(idx, 2)
        println("arr=${Arrays.toString(arr)} idx=$idx -> new=$new")
    }
}
