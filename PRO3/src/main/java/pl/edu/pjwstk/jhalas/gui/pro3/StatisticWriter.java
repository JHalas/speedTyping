package pl.edu.pjwstk.jhalas.gui.pro3;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

public class StatisticWriter {

    public static void writeStatistics(String filename, List<String> words, List<Integer> wpmList) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filename))) {
            for (int i = 0; i < words.size(); i++) {
                writer.println(words.get(i) + "\t" + wpmList.get(i) + "wpm");
            }
        } catch (IOException e) {
            System.out.println("save failed  " + e.getMessage());
        }
    }
}
