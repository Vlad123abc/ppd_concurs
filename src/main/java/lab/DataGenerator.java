package lab;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

public class DataGenerator {
    private static final int MAX_POINTS = 100;
    private static final int FRAUD_PROBABILITY = 2; // 2%
    private static final int NO_SOLUTION_PROBABILITY = 10; // 10%

    public static void main(String[] args) {
        int[] countries = {100, 85, 90, 80, 95}; // Numărul de concurenți pentru fiecare țară
        int problems = 10;

        Random random = new Random();

        for (int country = 1; country <= countries.length; country++) {
            for (int problem = 1; problem <= problems; problem++) {
                String fileName = "RezultateC" + country + "_P" + problem + ".txt";
                try (FileWriter writer = new FileWriter(fileName)) {
                    for (int id = 1; id <= countries[country - 1]; id++) {
                        int chance = random.nextInt(100);
                        if (chance < FRAUD_PROBABILITY) {
                            writer.write(id + ",-1\n"); // Fraudă
                        } else if (chance < FRAUD_PROBABILITY + NO_SOLUTION_PROBABILITY) {
                            // Nu scriem nimic dacă nu s-a rezolvat problema
                        } else {
                            int points = random.nextInt(MAX_POINTS + 1); // Punctaj între 0 și 100
                            writer.write(id + "," + points + "\n");
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        System.out.println("Datele de test au fost generate.");
    }
}
