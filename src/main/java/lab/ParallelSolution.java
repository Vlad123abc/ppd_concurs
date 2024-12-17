package lab;

import java.io.*;
import java.util.*;

public class ParallelSolution {
    private static final String OUTPUT_FILE = "Clasament.txt";

    public static void main(String[] args) {
//        long startTime = System.nanoTime(); // Pornirea cronometrului

        int p = 16; // Total număr de thread-uri
        int p_r = 2; // Numărul de Reader Threads
        int p_w = p - p_r; // Numărul de Worker Threads

        SharedQueue queue = new SharedQueue();
        Map<Integer, Integer> scores = Collections.synchronizedMap(new TreeMap<>(Collections.reverseOrder()));
        Set<Integer> disqualified = Collections.synchronizedSet(new HashSet<>());

        File folder = new File(".");
        File[] files = folder.listFiles((dir, name) -> name.matches("RezultateC\\d+_P\\d+\\.txt"));

        if (files == null || files.length == 0) {
            System.out.println("Nu s-au găsit fișiere.");
            return;
        }

        ReaderThread[] readers = new ReaderThread[p_r];
        for (int i = 0; i < p_r; i++) {
            File[] assignedFiles = assignFilesToReader(files, p_r, i); // Distribuim fișierele echilibrat
            readers[i] = new ReaderThread(queue, assignedFiles);
            readers[i].start();
        }

        WorkerThread[] workers = new WorkerThread[p_w];
        for (int i = 0; i < p_w; i++) {
            workers[i] = new WorkerThread(queue, scores, disqualified);
            workers[i].start();
        }

        for (ReaderThread reader : readers) {
            try {
                reader.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        queue.setFinished();

        for (WorkerThread worker : workers) {
            try {
                worker.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        // Scriem rezultatele în fișierul final
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(OUTPUT_FILE))) {
            scores.entrySet()
                    .stream()
                    .sorted((a, b) -> Integer.compare(b.getValue(), a.getValue()))
                    .forEach(entry -> {
                        try {
                            writer.write(entry.getKey() + "," + entry.getValue() + "\n");
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("Clasamentul final a fost generat.");

//        long endTime = System.nanoTime(); // Oprirea cronometrului
//        double durationInMillis = (endTime - startTime) / 1_000_000.0;
//        System.out.println("Durata execuției paralele: " + durationInMillis + " ms");
    }

    private static File[] assignFilesToReader(File[] files, int totalReaders, int readerIndex) {
        List<File> assignedFiles = new ArrayList<>();
        for (int i = readerIndex; i < files.length; i += totalReaders) {
            assignedFiles.add(files[i]);
        }
        return assignedFiles.toArray(new File[0]);
    }
}
