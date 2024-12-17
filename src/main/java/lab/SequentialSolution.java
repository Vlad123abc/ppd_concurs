package lab;

import java.io.*;
import java.util.*;

public class SequentialSolution {
    private static final String OUTPUT_FILE = "Clasament.txt";

    public static void main(String[] args) {
//        long startTime = System.nanoTime(); // Pornirea cronometrului

        Map<Integer, Integer> scores = new TreeMap<>(Collections.reverseOrder());
        Set<Integer> disqualified = new HashSet<>();

        File folder = new File(".");
        File[] files = folder.listFiles((dir, name) -> name.matches("RezultateC\\d+_P\\d+\\.txt"));

        if (files == null) {
            System.out.println("Nu s-au găsit fișiere.");
            return;
        }

        for (File file : files) {
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] parts = line.split(",");
                    int id = Integer.parseInt(parts[0]);
                    int score = Integer.parseInt(parts[1]);

                    if (score == -1) {
                        disqualified.add(id);
                        scores.remove(id);
                    } else if (!disqualified.contains(id)) {
                        scores.put(id, scores.getOrDefault(id, 0) + score);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

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
//        System.out.println("Durata execuției secvențiale: " + durationInMillis + " ms");
    }
}
