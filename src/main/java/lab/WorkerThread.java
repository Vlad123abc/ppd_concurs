package lab;

import java.util.Map;
import java.util.Set;

public class WorkerThread extends Thread {
    private final SharedQueue queue;
    private final Map<Integer, Integer> scores;
    private final Set<Integer> disqualified;

    public WorkerThread(SharedQueue queue, Map<Integer, Integer> scores, Set<Integer> disqualified) {
        this.queue = queue;
        this.scores = scores;
        this.disqualified = disqualified;
    }

    @Override
    public void run() {
        try {
            String line;
            while ((line = queue.remove()) != null) {
                String[] parts = line.split(",");
                int id = Integer.parseInt(parts[0]);
                int score = Integer.parseInt(parts[1]);

                synchronized (scores) {
                    if (score == -1) {
                        disqualified.add(id);
                        scores.remove(id);
                    } else if (!disqualified.contains(id)) {
                        scores.put(id, scores.getOrDefault(id, 0) + score);
                    }
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
