package lab;

public class BenchmarkRunner {
    public static void main(String[] args) {
        int numRuns = 10;
        double totalTime = 0;

        for (int i = 1; i <= numRuns; i++) {
            long startTime = System.nanoTime();

            // SequentialSolution.main(null);
            ParallelSolution.main(null);

            long endTime = System.nanoTime();
            double durationInMillis = (endTime - startTime) / 1_000_000.0;
            totalTime += durationInMillis;

            System.out.println("Durata execuției rundei " + i + ": " + durationInMillis + " ms");
        }

        double averageTime = totalTime / numRuns; // Calcularea mediei
        System.out.println("Timpul mediu pentru " + numRuns + " rulări: " + averageTime + " ms");
    }
}
