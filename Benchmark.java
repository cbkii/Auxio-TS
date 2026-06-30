import java.util.ArrayList;
import java.util.List;

public class Benchmark {
    public static void main(String[] args) {
        int N = 1000000;
        List<String> titles = new ArrayList<>(N);
        for (int i = 0; i < N; i++) {
            titles.add("my beautiful title " + i);
        }
        String query = "Beautiful";
        List<String> words = new ArrayList<>();
        words.add("beautiful");

        // Warmup
        for (int i = 0; i < 10; i++) {
            benchmarkSlow(titles, query, words);
            benchmarkFast(titles, query, words);
        }

        long t0 = System.nanoTime();
        benchmarkSlow(titles, query, words);
        long t1 = System.nanoTime();

        long t2 = System.nanoTime();
        benchmarkFast(titles, query, words);
        long t3 = System.nanoTime();

        System.out.println("Baseline: " + (t1 - t0) / 1000000.0 + " ms");
        System.out.println("Optimized: " + (t3 - t2) / 1000000.0 + " ms");
    }

    public static void benchmarkSlow(List<String> keys, String query, List<String> words) {
        int count = 0;
        for (String title : keys) {
            for (String word : words) {
                if (title.contains(word)) {
                    if (title.contains(query.toLowerCase())) {
                        count++;
                    } else {
                        count++;
                    }
                    break;
                }
            }
        }
    }

    public static void benchmarkFast(List<String> keys, String query, List<String> words) {
        int count = 0;
        String queryLowercase = query.toLowerCase();
        for (String title : keys) {
            for (String word : words) {
                if (title.contains(word)) {
                    if (title.contains(queryLowercase)) {
                        count++;
                    } else {
                        count++;
                    }
                    break;
                }
            }
        }
    }
}
