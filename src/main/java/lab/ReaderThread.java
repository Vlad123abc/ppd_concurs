package lab;

import java.io.*;

public class ReaderThread extends Thread {
    private final SharedQueue queue;
    private final File[] files;

    public ReaderThread(SharedQueue queue, File[] files) {
        this.queue = queue;
        this.files = files;
    }

    @Override
    public void run() {
        for (File file : files) {
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    queue.add(line);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
