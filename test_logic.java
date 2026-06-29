import java.util.Arrays;
import java.util.Random;

public class test_logic {

    static class BetterShuffleOrder {
        private final int[] shuffled;
        private final int[] indexInShuffled;

        public BetterShuffleOrder(int[] shuffled) {
            this.shuffled = shuffled;
            this.indexInShuffled = new int[shuffled.length];
            for (int i = 0; i < shuffled.length; i++) {
                indexInShuffled[shuffled[i]] = i;
            }
        }

        public BetterShuffleOrder cloneAndInsertOld(int insertionIndex, int insertionCount) {
            if (shuffled.length == 0) {
                int[] newShuffled = new int[insertionCount];
                for (int i = 0; i < insertionCount; i++) newShuffled[i] = i;
                return new BetterShuffleOrder(newShuffled);
            }

            int[] newShuffled = new int[shuffled.length + insertionCount];
            boolean isAppend = insertionIndex >= shuffled.length;
            int safeInsertionIndex = isAppend ? shuffled.length : insertionIndex;

            int pivot = -1;
            if (isAppend) {
                pivot = shuffled.length - 1;
            } else if (safeInsertionIndex > 0) {
                pivot = indexInShuffled[safeInsertionIndex - 1];
            }

            int newIdx = 0;
            for (int i = 0; i < shuffled.length; i++) {
                int currentIndex = shuffled[i];
                if (currentIndex >= safeInsertionIndex) {
                    currentIndex += insertionCount;
                }
                newShuffled[newIdx++] = currentIndex;

                if (i == pivot) {
                    for (int j = 0; j < insertionCount; j++) {
                        newShuffled[newIdx++] = safeInsertionIndex + j;
                    }
                }
            }

            if (pivot == -1) {
                for (int i = shuffled.length - 1; i >= 0; i--) {
                    newShuffled[i + insertionCount] = newShuffled[i];
                }
                for (int j = 0; j < insertionCount; j++) {
                    newShuffled[j] = safeInsertionIndex + j;
                }
            }

            return new BetterShuffleOrder(newShuffled);
        }

        public BetterShuffleOrder cloneAndInsertNew(int insertionIndex, int insertionCount) {
            if (shuffled.length == 0) {
                int[] newShuffled = new int[insertionCount];
                for (int i = 0; i < insertionCount; i++) newShuffled[i] = i;
                return new BetterShuffleOrder(newShuffled);
            }

            int[] newShuffled = new int[shuffled.length + insertionCount];
            int safeInsertionIndex = insertionIndex >= shuffled.length ? shuffled.length : insertionIndex;

            int insertPoint = 0;
            if (safeInsertionIndex == 0) {
                insertPoint = 0;
            } else if (safeInsertionIndex == shuffled.length) {
                insertPoint = shuffled.length;
            } else {
                insertPoint = indexInShuffled[safeInsertionIndex - 1] + 1;
            }

            for (int i = 0; i < insertPoint; i++) {
                int unshuffledIdx = shuffled[i];
                newShuffled[i] = unshuffledIdx >= safeInsertionIndex ? unshuffledIdx + insertionCount : unshuffledIdx;
            }

            for (int i = 0; i < insertionCount; i++) {
                newShuffled[insertPoint + i] = safeInsertionIndex + i;
            }

            for (int i = insertPoint; i < shuffled.length; i++) {
                int unshuffledIdx = shuffled[i];
                newShuffled[i + insertionCount] = unshuffledIdx >= safeInsertionIndex ? unshuffledIdx + insertionCount : unshuffledIdx;
            }

            return new BetterShuffleOrder(newShuffled);
        }

        public String toString() {
            return Arrays.toString(shuffled);
        }
    }

    public static void main(String[] args) {
        Random rand = new Random(42);
        for (int i = 0; i < 10000; i++) {
            int len = rand.nextInt(20) + 1;
            int[] arr = new int[len];
            for (int j = 0; j < len; j++) arr[j] = j;
            for (int j = 0; j < len; j++) {
                int swapIdx = rand.nextInt(len);
                int temp = arr[j];
                arr[j] = arr[swapIdx];
                arr[swapIdx] = temp;
            }

            int insertIdx = rand.nextInt(len + 2);
            int insertCount = rand.nextInt(5) + 1;

            BetterShuffleOrder order = new BetterShuffleOrder(arr);
            BetterShuffleOrder oldOrder = order.cloneAndInsertOld(insertIdx, insertCount);
            BetterShuffleOrder newOrder = order.cloneAndInsertNew(insertIdx, insertCount);
            if (!oldOrder.toString().equals(newOrder.toString())) {
                System.out.println("MISMATCH arr=" + Arrays.toString(arr) + " idx=" + insertIdx + " count=" + insertCount + " -> old=" + oldOrder + " new=" + newOrder);
            }
        }
        System.out.println("Fuzzing done.");
    }
}
