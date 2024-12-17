package lab;

import java.util.LinkedList;
import java.util.Queue;

public class SharedQueue {
    private final Queue<String> queue = new LinkedList<>();
    private boolean isFinished = false;

    public synchronized void add(String data) {
        queue.add(data);
        notifyAll();
    }

    public synchronized String remove() throws InterruptedException {
        while (queue.isEmpty() && !isFinished) {
            wait();
        }
        return queue.poll();
    }

    public synchronized void setFinished() {
        isFinished = true;
        notifyAll();
    }
}
